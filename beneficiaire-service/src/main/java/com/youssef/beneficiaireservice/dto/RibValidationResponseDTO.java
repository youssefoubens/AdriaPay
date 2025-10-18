package com.youssef.beneficiaireservice.dto;

import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import jakarta.validation.constraints.*;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de réponse pour la validation de RIB")
public class RibValidationResponseDTO {

    @Schema(description = "RIB vérifié", example = "123456789012345678901234")
    private String rib;

    @Schema(description = "Le RIB existe-t-il déjà?", example = "true")
    private Boolean exists;

    @Schema(description = "Message de validation", example = "Ce RIB est déjà associé à un bénéficiaire")
    private String message;
}
