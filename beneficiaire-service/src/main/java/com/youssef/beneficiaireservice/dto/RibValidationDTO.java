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
@Schema(description = "DTO pour vérifier l'existence d'un RIB")
public class RibValidationDTO {

    @NotBlank(message = "Le RIB est obligatoire")
    @Pattern(regexp = "^[0-9]{24}$", message = "Le RIB doit contenir exactement 24 chiffres")
    @Schema(description = "RIB à vérifier", example = "123456789012345678901234", required = true)
    private String rib;
}