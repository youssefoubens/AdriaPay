package com.youssef.beneficiaireservice.repositories;

import com.youssef.beneficiaireservice.entities.Beneficiaire;
import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BeneficiaireRepo extends JpaRepository<Beneficiaire, Long> {

    // === Basic Finders ===
    Optional<Beneficiaire> findByRib(String rib);

    List<Beneficiaire> findByType(TypeBeneficiaire type);

    List<Beneficiaire> findByActifTrue();

    List<Beneficiaire> findByActifFalse();

    boolean existsByRib(String rib);

    long countByType(TypeBeneficiaire type);

    long countByActifTrue();

    // === Custom Search Queries ===
    @Query("SELECT b FROM Beneficiaire b " +
            "WHERE LOWER(b.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(b.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(b.rib) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Beneficiaire> searchByNomOrPrenom(@Param("keyword") String keyword);

    // === Search with optional filters ===
    @Query("""
           SELECT b FROM Beneficiaire b
           WHERE (:type IS NULL OR b.type = :type)
             AND (:actif IS NULL OR b.actif = :actif)
             AND (:keyword IS NULL OR 
                 LOWER(b.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                 LOWER(b.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                 LOWER(b.rib) LIKE LOWER(CONCAT('%', :keyword, '%'))
             )
           """)
    List<Beneficiaire> searchWithFilters(@Param("type") TypeBeneficiaire type,
                                         @Param("actif") Boolean actif,
                                         @Param("keyword") String keyword);
}
