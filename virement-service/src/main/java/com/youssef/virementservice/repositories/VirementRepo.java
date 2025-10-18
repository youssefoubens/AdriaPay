package com.youssef.virementservice.repositories;

import com.youssef.virementservice.entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface VirementRepo extends JpaRepository<Virement, Long> {
    @Query("SELECT v FROM Virement v WHERE v.beneficiaireRib = :rib")
    List<Virement> findByBeneficiaireRib(@Param("rib") String rib);

    // Optionally, find active virements
    List<Virement> findByActifTrue();
}
