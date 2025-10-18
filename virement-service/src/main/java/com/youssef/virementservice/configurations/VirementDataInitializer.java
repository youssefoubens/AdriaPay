package com.youssef.virementservice.configurations;

import com.youssef.virementservice.dto.VirementDTO;
import com.youssef.virementservice.enums.TypeVirement;
import com.youssef.virementservice.models.Beneficiaire;
import com.youssef.virementservice.models.TypeBeneficiaire;
import com.youssef.virementservice.services.VirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class VirementDataInitializer {

    private final VirementService virementService;

    @Bean
    CommandLineRunner initVirements() {
        return args -> {
            // Sample Beneficiaires
            Beneficiaire benef1 = Beneficiaire.builder()
                    .id(1L)
                    .nom("ALAMI")
                    .prenom("Mohammed")
                    .rib("123456789012345678901234")
                    .type(TypeBeneficiaire.PHYSIQUE)
                    .email("m.alami@email.com")
                    .telephone("0612345678")
                    .adresse("123 Rue Mohammed V, Casablanca")
                    .actif(true)
                    .build();

            Beneficiaire benef2 = Beneficiaire.builder()
                    .id(2L)
                    .nom("BENSAID")
                    .prenom("Fatima")
                    .rib("987654321098765432109876")
                    .type(TypeBeneficiaire.MORALE)
                    .email("f.bensaid@email.com")
                    .telephone("0698765432")
                    .adresse("45 Avenue Hassan II, Rabat")
                    .actif(true)
                    .build();

            // Sample Virements
            VirementDTO v1 = VirementDTO.builder()
                    .beneficiaire(benef1)
                    .ribSource("111122223333444455556666")
                    .montant(new BigDecimal("2500.50"))
                    .description("Virement mensuel")
                    .dateVirement(LocalDateTime.now().minusDays(2))
                    .type(TypeVirement.NORMAL)
                    .actif(true)
                    .build();

            VirementDTO v2 = VirementDTO.builder()
                    .beneficiaire(benef2)
                    .ribSource("999988887777666655554444")
                    .montant(new BigDecimal("10000.00"))
                    .description("Paiement fournisseur")
                    .dateVirement(LocalDateTime.now().minusDays(1))
                    .type(TypeVirement.INSTANTANE)
                    .actif(true)
                    .build();

            // Save Virements
            virementService.saveVirement(v1);
            virementService.saveVirement(v2);

            System.out.println("Sample virements initialized!");
        };
    }
}
