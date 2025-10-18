package com.youssef.beneficiaireservice.dto;

import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import jakarta.validation.constraints.*;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO simplifié pour les listes de bénéficiaires")
public class BeneficiaireSummaryDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom complet", example = "ALAMI Mohammed")
    private String nomComplet;

    @Schema(description = "RIB", example = "123456789012345678901234")
    private String rib;

    @Schema(description = "Type", example = "PHYSIQUE")
    private TypeBeneficiaire type;

    @Schema(description = "Statut actif", example = "true")
    private Boolean actif;

    // Méthode utilitaire pour construire le nom complet
    public static String buildNomComplet(String nom, String prenom) {
        return nom + " " + prenom;
    }
}
