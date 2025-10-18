package com.youssef.virementservice.client;


import com.youssef.virementservice.models.Beneficiaire;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(name = "beneficiaire-service") // Eureka service name
public interface BeneficiaireClient {

    @GetMapping("/api/beneficiaires/{id}")
    Beneficiaire getById(@PathVariable("id") Long id);

    // You can add more endpoints if needed
    @GetMapping("/api/beneficiaires/by-rib")
    Beneficiaire getBeneficiaireByRib(@RequestParam("rib") String rib);

    // Optional: fetch all beneficiaries
    @GetMapping("/api/beneficiaires")
    List<Beneficiaire> getAllBeneficiaires();
}
