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
@Schema(description = "DTO pour l'historique des conversations")
public class ChatHistoryDTO {

    @Schema(description = "ID du message")
    private UUID id;

    @Schema(description = "Question")
    private String question;

    @Schema(description = "Réponse")
    private String answer;

    @Schema(description = "Date de création")
    private LocalDateTime createdAt;

    @Schema(description = "Temps de réponse en ms")
    private Long responseTimeMs;
}

