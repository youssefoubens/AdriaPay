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
@Schema(description = "DTO de réponse après upload d'un document")
public class DocumentUploadResponseDTO {

    @Schema(description = "ID du document", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nom du fichier", example = "conditions-generales.pdf")
    private String filename;

    @Schema(description = "Taille du fichier en octets", example = "524288")
    private Long fileSize;

    @Schema(description = "Nombre de pages", example = "15")
    private Integer pageCount;

    @Schema(description = "Statut du traitement", example = "PROCESSING")
    private String status;

    @Schema(description = "Date d'upload")
    private LocalDateTime uploadDate;

    @Schema(description = "Message de statut", example = "Document uploadé et en cours de traitement")
    private String message;
}