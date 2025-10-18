package com.youssef.beneficiaireservice.dto;

import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import jakarta.validation.constraints.*;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour mettre à jour un bénéficiaire existant (mise à jour partielle)")
public class BeneficiaireUpdateDTO {

    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Schema(description = "Nouveau nom du bénéficiaire", example = "ALAMI")
    private String nom;

    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    @Schema(description = "Nouveau prénom du bénéficiaire", example = "Mohammed")
    private String prenom;

    @Email(message = "Email invalide")
    @Schema(description = "Nouvel email du bénéficiaire", example = "m.alami@email.com")
    private String email;

    @Pattern(regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Numéro de téléphone marocain invalide")
    @Schema(description = "Nouveau téléphone du bénéficiaire", example = "0612345678")
    private String telephone;

    @Size(max = 200, message = "L'adresse ne peut pas dépasser 200 caractères")
    @Schema(description = "Nouvelle adresse du bénéficiaire", example = "123 Rue Mohammed V, Casablanca")
    private String adresse;

    @Schema(description = "Nouveau statut actif", example = "true")
    private Boolean actif;
}

