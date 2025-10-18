package com.youssef.chatbotservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// ==================== Chat Request DTO ====================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour envoyer une question au chatbot")
public class ChatRequestDTO {

    @NotBlank(message = "La question est obligatoire")
    @Size(min = 3, max = 1000, message = "La question doit contenir entre 3 et 1000 caractères")
    @Schema(description = "Question posée au chatbot", example = "Quels sont les frais de virement international ?")
    private String question;

    @Schema(description = "ID de session pour maintenir le contexte", example = "session-123")
    private String sessionId;

    @Schema(description = "ID de l'utilisateur", example = "user-456")
    private String userId;
}