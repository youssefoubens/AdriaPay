package com.youssef.chatbotservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour les statistiques du système RAG")
public class RagStatsDTO {

    @Schema(description = "Nombre total de documents", example = "25")
    private Long totalDocuments;

    @Schema(description = "Nombre de documents traités", example = "23")
    private Long processedDocuments;

    @Schema(description = "Nombre de chunks dans le vector store", example = "1250")
    private Long totalChunks;

    @Schema(description = "Nombre total de conversations", example = "450")
    private Long totalConversations;

    @Schema(description = "Temps de réponse moyen en ms", example = "1350")
    private Double averageResponseTime;
}