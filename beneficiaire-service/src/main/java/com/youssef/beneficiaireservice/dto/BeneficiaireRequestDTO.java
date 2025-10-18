package com.youssef.beneficiaireservice.dto;

import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import jakarta.validation.constraints.*;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

// ==================== DTO pour la création d'un bénéficiaire ====================
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour créer un nouveau bénéficiaire")
public class BeneficiaireRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Schema(description = "Nom du bénéficiaire", example = "ALAMI", required = true)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    @Schema(description = "Prénom du bénéficiaire", example = "Mohammed", required = true)
    private String prenom;

    @NotBlank(message = "Le RIB est obligatoire")
    @Pattern(regexp = "^[0-9]{24}$", message = "Le RIB doit contenir exactement 24 chiffres")
    @Schema(description = "RIB du bénéficiaire (24 chiffres)", example = "123456789012345678901234", required = true)
    private String rib;

    @NotNull(message = "Le type est obligatoire")
    @Schema(description = "Type de bénéficiaire", example = "PHYSIQUE", required = true,
            allowableValues = {"PHYSIQUE", "MORALE"})
    private TypeBeneficiaire type;

    @Email(message = "Email invalide")
    @Schema(description = "Email du bénéficiaire", example = "m.alami@email.com")
    private String email;

    @Pattern(regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Numéro de téléphone marocain invalide (format: 0612345678 ou +212612345678)")
    @Schema(description = "Téléphone du bénéficiaire", example = "0612345678")
    private String telephone;

    @Size(max = 200, message = "L'adresse ne peut pas dépasser 200 caractères")
    @Schema(description = "Adresse du bénéficiaire", example = "123 Rue Mohammed V, Casablanca")
    private String adresse;
}
