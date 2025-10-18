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
@Schema(description = "DTO de réponse du chatbot")
public class ChatResponseDTO {

    @Schema(description = "Réponse du chatbot", example = "Les frais de virement international sont...")
    private String answer;

    @Schema(description = "Question originale", example = "Quels sont les frais de virement ?")
    private String question;

    @Schema(description = "ID de la session", example = "session-123")
    private String sessionId;

    @Schema(description = "Sources utilisées pour générer la réponse")
    private List<String> sources;

    @Schema(description = "Temps de réponse en millisecondes", example = "1250")
    private Long responseTimeMs;

    @Schema(description = "Horodatage de la réponse")
    private LocalDateTime timestamp;

    @Schema(description = "Niveau de confiance de la réponse (0-1)", example = "0.95")
    private Double confidence;
}
