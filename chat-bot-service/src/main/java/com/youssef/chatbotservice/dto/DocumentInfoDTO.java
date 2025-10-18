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
@Schema(description = "DTO d'information sur un document")
public class DocumentInfoDTO {

    @Schema(description = "ID du document")
    private UUID id;

    @Schema(description = "Nom du fichier")
    private String filename;

    @Schema(description = "Taille du fichier")
    private Long fileSize;

    @Schema(description = "Nombre de pages")
    private Integer pageCount;

    @Schema(description = "Document traité?")
    private Boolean processed;

    @Schema(description = "Date d'upload")
    private LocalDateTime uploadDate;
}

