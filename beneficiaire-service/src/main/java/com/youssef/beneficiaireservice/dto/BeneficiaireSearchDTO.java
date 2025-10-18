package com.youssef.beneficiaireservice.dto;


import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import jakarta.validation.constraints.*;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour les critères de recherche de bénéficiaires")
public class BeneficiaireSearchDTO {

    @Schema(description = "Mot-clé pour rechercher dans nom et prénom", example = "ALAMI")
    private String keyword;

    @Schema(description = "Filtrer par type de bénéficiaire", example = "PHYSIQUE")
    private TypeBeneficiaire type;

    @Schema(description = "Filtrer par statut actif", example = "true")
    private Boolean actif;
}

