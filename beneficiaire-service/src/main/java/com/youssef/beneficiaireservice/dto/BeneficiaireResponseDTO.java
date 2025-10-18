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
@Schema(description = "DTO de réponse contenant les informations d'un bénéficiaire")
public class BeneficiaireResponseDTO {

    @Schema(description = "Identifiant unique du bénéficiaire", example = "1")
    private Long id;

    @Schema(description = "Nom du bénéficiaire", example = "ALAMI")
    private String nom;

    @Schema(description = "Prénom du bénéficiaire", example = "Mohammed")
    private String prenom;

    @Schema(description = "RIB du bénéficiaire", example = "123456789012345678901234")
    private String rib;

    @Schema(description = "Type de bénéficiaire", example = "PHYSIQUE")
    private TypeBeneficiaire type;

    @Schema(description = "Email du bénéficiaire", example = "m.alami@email.com")
    private String email;

    @Schema(description = "Téléphone du bénéficiaire", example = "0612345678")
    private String telephone;

    @Schema(description = "Adresse du bénéficiaire", example = "123 Rue Mohammed V, Casablanca")
    private String adresse;

    @Schema(description = "Statut actif du bénéficiaire", example = "true")
    private Boolean actif;

    @Schema(description = "Date de création", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Date de dernière modification", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
