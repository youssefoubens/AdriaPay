
package com.youssef.chatbotservice.services;

import com.youssef.chatbotservice.dto.*;
import com.youssef.chatbotservice.entities.ChatMessage;
import com.youssef.chatbotservice.entities.Document;
import com.youssef.chatbotservice.repository.ChatMessageRepository;
import com.youssef.chatbotservice.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final DocumentRepository documentRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Value("${chatbot.pdf.storage-path}")
    private String pdfStoragePath;

    // ==================== Chat avec RAG ====================

    public ChatResponseDTO chat(ChatRequestDTO request) {
        log.info("Processing chat request: {}", request.getQuestion());
        long startTime = System.currentTimeMillis();

        try {
            // 1. Rechercher les documents pertinents dans le vector store avec gestion d'erreur
            List<org.springframework.ai.document.Document> relevantDocs = searchRelevantDocuments(request.getQuestion());

            log.debug("Found {} relevant documents", relevantDocs.size());

            // 2. Construire le contexte à partir des documents
            String context = relevantDocs.stream()
                    .map(org.springframework.ai.document.Document::getContent)
                    .collect(Collectors.joining("\n\n"));

            // 3. Construire le prompt avec le contexte
            String systemPrompt = buildSystemPrompt(context);

            // 4. Appeler le modèle LLM
            Prompt prompt = new Prompt(systemPrompt + "\n\nQuestion: " + request.getQuestion());
            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
            String answer = response.getResult().getOutput().getContent();

            long responseTime = System.currentTimeMillis() - startTime;

            // 5. Sauvegarder la conversation
            ChatMessage chatMessage = ChatMessage.builder()
                    .sessionId(request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString())
                    .question(request.getQuestion())
                    .answer(answer)
                    .userId(request.getUserId())
                    .responseTimeMs(responseTime)
                    .sources(extractSources(relevantDocs))
                    .build();

            chatMessageRepository.save(chatMessage);

            // 6. Construire la réponse
            return ChatResponseDTO.builder()
                    .answer(answer)
                    .question(request.getQuestion())
                    .sessionId(chatMessage.getSessionId())
                    .sources(extractSourcesList(relevantDocs))
                    .responseTimeMs(responseTime)
                    .timestamp(LocalDateTime.now())
                    .confidence(calculateConfidence(relevantDocs))
                    .build();

        } catch (Exception e) {
            log.error("Error processing chat request", e);
            return handleChatError(request, startTime, e);
        }
    }

    // ==================== Méthode de recherche avec fallback ====================

    private List<org.springframework.ai.document.Document> searchRelevantDocuments(String question) {
        try {
            // Tentative de recherche vectorielle normale
            return vectorStore.similaritySearch(
                    SearchRequest.builder().query(question).topK(3).build()
            );
        } catch (Exception e) {
            log.error("Vector search failed, trying alternative approach", e);

            try {
                // Fallback: recherche simple sans seuil de similarité
                return vectorStore.similaritySearch(
                        SearchRequest.builder().query(question).build()
                );
            } catch (Exception fallbackError) {
                log.error("Fallback vector search also failed", fallbackError);

                // Dernière option: retourner une liste vide
                return new ArrayList<>();
            }
        }
    }

    // ==================== Gestion d'erreur pour le chat ====================

    private ChatResponseDTO handleChatError(ChatRequestDTO request, long startTime, Exception e) {
        long responseTime = System.currentTimeMillis() - startTime;

        String errorAnswer = "Je m'excuse, mais je rencontre actuellement des difficultés techniques. " +
                "Veuillez réessayer dans quelques instants ou contacter le support technique.";

        // Sauvegarder même l'erreur pour le suivi
        try {
            ChatMessage errorMessage = ChatMessage.builder()
                    .sessionId(request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString())
                    .question(request.getQuestion())
                    .answer(errorAnswer)
                    .userId(request.getUserId())
                    .responseTimeMs(responseTime)
                    .sources("ERROR: " + e.getMessage())
                    .build();

            chatMessageRepository.save(errorMessage);

            return ChatResponseDTO.builder()
                    .answer(errorAnswer)
                    .question(request.getQuestion())
                    .sessionId(errorMessage.getSessionId())
                    .sources(new ArrayList<>())
                    .responseTimeMs(responseTime)
                    .timestamp(LocalDateTime.now())
                    .confidence(0.0)
                    .build();

        } catch (Exception saveError) {
            log.error("Failed to save error message", saveError);
            throw new RuntimeException("Service temporairement indisponible", e);
        }
    }

    // ==================== Méthode utilitaire pour le prompt ====================

    private String buildSystemPrompt(String context) {
        if (context.trim().isEmpty()) {
            return """
                Tu es un assistant bancaire intelligent. 
                Je n'ai pas trouvé d'informations spécifiques dans ma base de connaissances 
                pour répondre à cette question. Peux-tu donner une réponse générale basée 
                sur les bonnes pratiques bancaires en précisant que pour des informations 
                spécifiques, il vaut mieux contacter directement la banque.
                Réponds en français de manière claire et professionnelle.
                """;
        }

        return """
            Tu es un assistant bancaire intelligent. Utilise UNIQUEMENT les informations 
            fournies dans le contexte ci-dessous pour répondre à la question.
            Si l'information n'est pas dans le contexte, dis-le clairement.
            Réponds en français de manière claire et professionnelle.
            
            Contexte:
            %s
            """.formatted(context);
    }

    // ==================== Upload et traitement de PDF ====================

    public DocumentUploadResponseDTO uploadDocument(MultipartFile file) {
        log.info("Uploading document: {}", file.getOriginalFilename());

        Path filePath = null;
        try {
            // 1. Vérifier si le fichier existe déjà en base de données
            if (documentRepository.existsByFilename(file.getOriginalFilename())) {
                log.warn("Document with filename {} already exists in database", file.getOriginalFilename());
                throw new RuntimeException("Un document avec ce nom existe déjà");
            }

            // 2. Créer le répertoire de stockage s'il n'existe pas
            Path storagePath = Paths.get(pdfStoragePath);
            if (!Files.exists(storagePath)) {
                log.info("Creating storage directory: {}", storagePath);
                Files.createDirectories(storagePath);
            }

            // 3. Préparer le chemin du fichier
            String filename = file.getOriginalFilename();
            filePath = storagePath.resolve(filename);

            log.info("Attempting to save file to: {}", filePath.toAbsolutePath());

            // 4. Sauvegarder le fichier avec remplacement si nécessaire
            try {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                log.info("File successfully saved to: {}", filePath.toAbsolutePath());
            } catch (IOException copyException) {
                log.error("Failed to copy file to: {}", filePath.toAbsolutePath(), copyException);
                throw new RuntimeException("Erreur lors de la sauvegarde du fichier", copyException);
            }

            // 5. Vérifier que le fichier a bien été créé
            if (!Files.exists(filePath)) {
                log.error("File was not created at expected location: {}", filePath.toAbsolutePath());
                throw new RuntimeException("Le fichier n'a pas pu être sauvegardé");
            }

            log.info("File size on disk: {} bytes", Files.size(filePath));

            // 6. Créer l'entité Document
            Document document = Document.builder()
                    .filename(filename)
                    .filePath(filePath.toAbsolutePath().toString())
                    .fileSize(file.getSize())
                    .processed(false)
                    .build();

            document = documentRepository.save(document);
            log.info("Document entity saved with ID: {}", document.getId());

            // 7. Traiter le document en arrière-plan
            processDocument(document);

            return DocumentUploadResponseDTO.builder()
                    .id(document.getId())
                    .filename(document.getFilename())
                    .fileSize(document.getFileSize())
                    .uploadDate(document.getUploadDate())
                    .status("PROCESSING")
                    .message("Document uploadé et en cours de traitement")
                    .build();

        } catch (IOException e) {
            log.error("IOException during document upload", e);
            // Nettoyer le fichier partiellement créé en cas d'erreur
            if (filePath != null && Files.exists(filePath)) {
                try {
                    Files.delete(filePath);
                    log.info("Cleaned up partially created file: {}", filePath);
                } catch (IOException cleanupException) {
                    log.warn("Failed to cleanup file: {}", filePath, cleanupException);
                }
            }
            throw new RuntimeException("Erreur lors de l'upload du document", e);
        } catch (Exception e) {
            log.error("General error during document upload", e);
            // Nettoyer le fichier en cas d'erreur
            if (filePath != null && Files.exists(filePath)) {
                try {
                    Files.delete(filePath);
                    log.info("Cleaned up file after error: {}", filePath);
                } catch (IOException cleanupException) {
                    log.warn("Failed to cleanup file: {}", filePath, cleanupException);
                }
            }
            throw e;
        }
    }

    // ==================== Traitement du document PDF ====================

    private void processDocument(Document document) {
        log.info("Processing document: {}", document.getFilename());

        try {
            // 1. Vérifier que le fichier existe
            File pdfFile = new File(document.getFilePath());
            if (!pdfFile.exists()) {
                log.error("PDF file not found at path: {}", document.getFilePath());
                throw new RuntimeException("Fichier PDF introuvable");
            }

            log.info("PDF file found at: {}", pdfFile.getAbsolutePath());
            log.info("PDF file size: {} bytes", pdfFile.length());

            // 2. Lire le PDF
            DocumentReader reader = new PagePdfDocumentReader(pdfFile.toURI().toURL().toString());
            List<org.springframework.ai.document.Document> documents = reader.get();

            log.info("Extracted {} pages from PDF", documents.size());

            // 3. Stocker dans le vector store avec gestion d'erreur
            try {
                vectorStore.add(documents);
                log.info("Documents successfully added to vector store");
            } catch (Exception vectorStoreError) {
                log.error("Failed to add documents to vector store", vectorStoreError);
                throw vectorStoreError;
            }

            // 4. Mettre à jour le statut
            document.setProcessed(true);
            document.setPageCount(documents.size());
            documentRepository.save(document);

            log.info("Document processed successfully: {}", document.getFilename());

        } catch (Exception e) {
            log.error("Error processing document: {}", document.getFilename(), e);
            document.setProcessed(false);
            documentRepository.save(document);
            throw new RuntimeException("Erreur lors du traitement du document", e);
        }
    }

    // ==================== Récupérer l'historique ====================

    @Transactional(readOnly = true)
    public List<ChatHistoryDTO> getChatHistory(String sessionId) {
        log.debug("Fetching chat history for session: {}", sessionId);

        List<ChatMessage> messages = chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);

        return messages.stream()
                .map(msg -> ChatHistoryDTO.builder()
                        .id(msg.getId())
                        .question(msg.getQuestion())
                        .answer(msg.getAnswer())
                        .createdAt(msg.getCreatedAt())
                        .responseTimeMs(msg.getResponseTimeMs())
                        .build())
                .collect(Collectors.toList());
    }

    // ==================== Récupérer les documents ====================

    @Transactional(readOnly = true)
    public List<DocumentInfoDTO> getAllDocuments() {
        log.debug("Fetching all documents");

        return documentRepository.findAll().stream()
                .map(doc -> DocumentInfoDTO.builder()
                        .id(doc.getId())
                        .filename(doc.getFilename())
                        .fileSize(doc.getFileSize())
                        .pageCount(doc.getPageCount())
                        .processed(doc.getProcessed())
                        .uploadDate(doc.getUploadDate())
                        .build())
                .collect(Collectors.toList());
    }

    // ==================== Statistiques ====================

    @Transactional(readOnly = true)
    public RagStatsDTO getStats() {
        log.debug("Calculating RAG stats");

        return RagStatsDTO.builder()
                .totalDocuments(documentRepository.count())
                .processedDocuments(documentRepository.countByProcessed(true))
                .totalConversations(chatMessageRepository.countUniqueSessions())
                .averageResponseTime(chatMessageRepository.calculateAverageResponseTime())
                .build();
    }

    // ==================== Méthodes utilitaires ====================

    private String extractSources(List<org.springframework.ai.document.Document> docs) {
        return docs.stream()
                .map(doc -> doc.getMetadata().get("source"))
                .filter(source -> source != null)
                .map(Object::toString)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private List<String> extractSourcesList(List<org.springframework.ai.document.Document> docs) {
        return docs.stream()
                .map(doc -> doc.getMetadata().get("source"))
                .filter(source -> source != null)
                .map(Object::toString)
                .distinct()
                .collect(Collectors.toList());
    }

    private Double calculateConfidence(List<org.springframework.ai.document.Document> docs) {
        if (docs.isEmpty()) {
            return 0.0;
        }
        // Calculer la confiance basée sur le nombre et la pertinence des documents
        return Math.min(1.0, docs.size() * 0.2);
    }
}