package com.youssef.beneficiaireservice.config;

import com.youssef.beneficiaireservice.entities.Beneficiaire;
import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import com.youssef.beneficiaireservice.repositories.BeneficiaireRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(BeneficiaireRepo repo) {
        return args -> {
            if (repo.count() == 0) {

                Beneficiaire b1 = Beneficiaire.builder()
                        .nom("Youssef")
                        .prenom("Benss")
                        .rib("123456789012345678901234")
                        .type(TypeBeneficiaire.PHYSIQUE)
                        .email("youssef.benss@example.com")
                        .telephone("+212612345678")
                        .adresse("Casablanca, Maroc")
                        .actif(true)
                        .build();

                Beneficiaire b2 = Beneficiaire.builder()
                        .nom("TechCorp")
                        .prenom("SARL")
                        .rib("987654321098765432109876")
                        .type(TypeBeneficiaire.MORALE)
                        .email("contact@techcorp.ma")
                        .telephone("+212522334455")
                        .adresse("Technopark, Casablanca")
                        .actif(true)
                        .build();

                Beneficiaire b3 = Beneficiaire.builder()
                        .nom("Fatima")
                        .prenom("El Idrissi")
                        .rib("112233445566778899001122")
                        .type(TypeBeneficiaire.PHYSIQUE)
                        .email("fatima.elidrissi@example.com")
                        .telephone("+212655443322")
                        .adresse("Rabat, Maroc")
                        .actif(false)
                        .build();

                repo.save(b1);
                repo.save(b2);
                repo.save(b3);

                System.out.println("✅ H2 database initialized with sample bénéficiaires.");
            } else {
                System.out.println("ℹ️ Database already contains bénéficiaires, skipping initialization.");
            }
        };
    }
}
