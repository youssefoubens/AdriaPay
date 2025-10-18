package com.youssef.virementservice.repositories;

import com.youssef.virementservice.entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface VirementRepo extends JpaRepository<Virement, Long> {
    Collection<Object> findByBeneficiaireRib(String rib);
}
