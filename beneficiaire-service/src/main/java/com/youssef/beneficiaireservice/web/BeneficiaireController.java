package com.youssef.beneficiaireservice.web;



import com.youssef.beneficiaireservice.dto.*;

import com.youssef.beneficiaireservice.entities.Beneficiaire;
import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;
import com.youssef.beneficiaireservice.mapper.BeneficiaireMapper;
import com.youssef.beneficiaireservice.service.IBeneficiaireService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaires")
@RequiredArgsConstructor
@Tag(name = "Bénéficiaires", description = "API de gestion des bénéficiaires bancaires")
public class BeneficiaireController {

    private final IBeneficiaireService beneficiaireService;
    private final BeneficiaireMapper beneficiaireMapper;

    // === Create ===
    @Operation(summary = "Créer un nouveau bénéficiaire")
    @PostMapping
    public ResponseEntity<BeneficiaireResponseDTO> createBeneficiaire(@RequestBody BeneficiaireRequestDTO dto) {
        BeneficiaireResponseDTO saved = beneficiaireService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // === Read All ===
    @Operation(summary = "Lister tous les bénéficiaires")
    @GetMapping
    public ResponseEntity<List<BeneficiaireResponseDTO>> getAllBeneficiaires() {
        return ResponseEntity.ok(beneficiaireService.getAll());
    }

    // === Read by ID ===
    @Operation(summary = "Obtenir un bénéficiaire par ID")
    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaireResponseDTO> getBeneficiaireById(@PathVariable Long id) {
        BeneficiaireResponseDTO dto = beneficiaireService.getById(id);
        return ResponseEntity.ok(dto);
    }

    // === Update ===
    @Operation(summary = "Mettre à jour un bénéficiaire")
    @PutMapping("/{id}")
    public ResponseEntity<BeneficiaireResponseDTO> updateBeneficiaire(
            @PathVariable Long id,
            @RequestBody BeneficiaireUpdateDTO dto) {

        BeneficiaireResponseDTO updated = beneficiaireService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // === Delete ===
    @Operation(summary = "Supprimer un bénéficiaire")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiaire(@PathVariable Long id) {
        beneficiaireService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // === Search ===
    @Operation(summary = "Rechercher un bénéficiaire par mot clé")
    @GetMapping("/search")
    public ResponseEntity<List<BeneficiaireResponseDTO>> searchBeneficiaires(
            @RequestParam(required = false) String keyword
           ) {

        List<BeneficiaireResponseDTO> result = beneficiaireService.search(keyword);
        return ResponseEntity.ok(result);
    }

    // === Count ===
    @Operation(summary = "Compter les bénéficiaires actifs ou par type")
    @GetMapping("/count")
    public ResponseEntity<Long> countBeneficiaires(
            @RequestParam(required = false) TypeBeneficiaire type,
            @RequestParam(required = false, defaultValue = "false") boolean onlyActive) {

        long count = beneficiaireService.countActifs();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/by-rib")
    public BeneficiaireResponseDTO getBeneficiaireByRib(@RequestParam String rib) {
        return beneficiaireService.getBeneficiaireByRib(rib);
    }

}

