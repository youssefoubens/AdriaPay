package com.youssef.chatbotservice.web;


import com.youssef.chatbotservice.dto.*;
import com.youssef.chatbotservice.services.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "API du chatbot RAG pour les services bancaires")
public class ChatController {

    private final RagService ragService;

    // ==================== Endpoint Chat ====================

    @PostMapping("/chat")
    @Operation(summary = "Poser une question au chatbot",
            description = "Envoie une question et reçoit une réponse générée par IA avec RAG")
    public ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatRequestDTO request) {
        ChatResponseDTO response = ragService.chat(request);
        return ResponseEntity.ok(response);}
    @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload un document PDF",
            description = "Upload un document PDF qui sera traité et indexé dans le système RAG")
    public ResponseEntity<DocumentUploadResponseDTO> uploadDocument(
            @Parameter(description = "Fichier PDF à uploader")
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().build();
        }

        DocumentUploadResponseDTO response = ragService.uploadDocument(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================== Endpoint Liste Documents ====================

    @GetMapping("/documents")
    @Operation(summary = "Récupérer tous les documents",
            description = "Retourne la liste de tous les documents uploadés")
    public ResponseEntity<List<DocumentInfoDTO>> getAllDocuments() {
        List<DocumentInfoDTO> documents = ragService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    // ==================== Endpoint Historique ====================

    @GetMapping("/history/{sessionId}")
    @Operation(summary = "Récupérer l'historique d'une session",
            description = "Retourne l'historique complet des conversations d'une session")
    public ResponseEntity<List<ChatHistoryDTO>> getChatHistory(
            @Parameter(description = "ID de la session")
            @PathVariable String sessionId) {
        List<ChatHistoryDTO> history = ragService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    // ==================== Endpoint Statistiques ====================

    @GetMapping("/stats")
    @Operation(summary = "Récupérer les statistiques du système",
            description = "Retourne les statistiques du système RAG")
    public ResponseEntity<RagStatsDTO> getStats() {
        RagStatsDTO stats = ragService.getStats();
        return ResponseEntity.ok(stats);
    }
}

