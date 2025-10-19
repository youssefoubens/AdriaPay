package com.youssef.virementservice.client;

import com.youssef.virementservice.models.Beneficiaire;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class BeneficiaireClientFallback implements BeneficiaireClient {

    @Override
    public Beneficiaire getById(Long id) {
        log.error("Beneficiaire service is down! Cannot fetch beneficiaire with ID: {}", id);
        return null; // ou retourner un objet par défaut
    }

    @Override
    public Beneficiaire getBeneficiaireByRib(String rib) {
        log.error("Beneficiaire service is down! Cannot fetch beneficiaire with RIB: {}", rib);
        return null;
    }

    @Override
    public List<Beneficiaire> getAllBeneficiaires() {
        log.error("Beneficiaire service is down! Cannot fetch beneficiaires");
        return Collections.emptyList();
    }
}