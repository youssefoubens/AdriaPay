package com.youssef.virementservice.client;


import com.youssef.virementservice.models.Beneficiaire;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "beneficiaire-service") // Eureka service name
public interface BeneficiaireClient {

    @GetMapping("/api/beneficiaires/{id}")
    Beneficiaire getById(@PathVariable("id") Long id);

    // You can add more endpoints if needed
}
