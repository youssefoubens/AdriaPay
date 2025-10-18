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
import java.time.LocalDateTime;
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
            // 1. Rechercher les documents pertinents dans le vector store
            List<org.springframework.ai.document.Document> relevantDocs = vectorStore.similaritySearch(
                    SearchRequest.query(request.getQuestion())
                            .withTopK(5)  // Top 5 résultats les plus pertinents
                            .withSimilarityThreshold(0.7)  // Seuil de similarité
            );

            log.debug("Found {} relevant documents", relevantDocs.size());

            // 2. Construire le contexte à partir des documents
            String context = relevantDocs.stream()
                    .map(org.springframework.ai.document.Document::getContent)
                    .collect(Collectors.joining("\n\n"));

            // 3. Construire le prompt avec le contexte
            String systemPrompt = """
                Tu es un assistant bancaire intelligent. Utilise UNIQUEMENT les informations 
                fournies dans le contexte ci-dessous pour répondre à la question.
                Si l'information n'est pas dans le contexte, dis-le clairement.
                Réponds en français de manière claire et professionnelle.
                
                Contexte:
                %s
                """.formatted(context);

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
            throw new RuntimeException("Erreur lors du traitement de la question", e);
        }
    }

    // ==================== Upload et traitement de PDF ====================

    public DocumentUploadResponseDTO uploadDocument(MultipartFile file) {
        log.info("Uploading document: {}", file.getOriginalFilename());

        try {
            // 1. Vérifier si le fichier existe déjà
            if (documentRepository.existsByFilename(file.getOriginalFilename())) {
                throw new RuntimeException("Un document avec ce nom existe déjà");
            }

            // 2. Créer le répertoire de stockage s'il n'existe pas
            Path storagePath = Paths.get(pdfStoragePath);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }

            // 3. Sauvegarder le fichier
            String filename = file.getOriginalFilename();
            Path filePath = storagePath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // 4. Créer l'entité Document
            Document document = Document.builder()
                    .filename(filename)
                    .filePath(filePath.toString())
                    .fileSize(file.getSize())
                    .processed(false)
                    .build();

            document = documentRepository.save(document);

            // 5. Traiter le document en arrière-plan (asynchrone recommandé)
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
            log.error("Error uploading document", e);
            throw new RuntimeException("Erreur lors de l'upload du document", e);
        }
    }

    // ==================== Traitement du document PDF ====================

    private void processDocument(Document document) {
        log.info("Processing document: {}", document.getFilename());

        try {
            // 1. Lire le PDF
            File pdfFile = new File(document.getFilePath());
            DocumentReader reader = new PagePdfDocumentReader(pdfFile.toURI().toURL().toString());
            List<org.springframework.ai.document.Document> documents = reader.get();

            log.info("Extracted {} pages from PDF", documents.size());

            // 2. Stocker dans le vector store
            vectorStore.add(documents);

            // 3. Mettre à jour le statut
            document.setProcessed(true);
            document.setPageCount(documents.size());
            documentRepository.save(document);

            log.info("Document processed successfully: {}", document.getFilename());

        } catch (Exception e) {
            log.error("Error processing document", e);
            document.setProcessed(false);
            documentRepository.save(document);
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