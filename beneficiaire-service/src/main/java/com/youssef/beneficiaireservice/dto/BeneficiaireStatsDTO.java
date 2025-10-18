package com.youssef.beneficiaireservice.dto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour les statistiques des bénéficiaires")
public class BeneficiaireStatsDTO {

    @Schema(description = "Nombre total de bénéficiaires", example = "150")
    private Long totalBeneficiaires;

    @Schema(description = "Nombre de bénéficiaires actifs", example = "140")
    private Long beneficiairesActifs;

    @Schema(description = "Nombre de bénéficiaires inactifs", example = "10")
    private Long beneficiairesInactifs;

    @Schema(description = "Nombre de personnes physiques", example = "120")
    private Long personnesPhysiques;

    @Schema(description = "Nombre de personnes morales", example = "30")
    private Long personnesMorales;
}

