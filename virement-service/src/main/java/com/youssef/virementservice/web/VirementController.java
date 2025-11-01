package com.youssef.virementservice.web;

import com.youssef.virementservice.dto.VirementDTO;
import com.youssef.virementservice.services.VirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RefreshScope
@RestController
@RequestMapping("/api/virements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VirementController {

    private final VirementService virementService;

    // Create a new virement
    @PostMapping
    public ResponseEntity<VirementDTO> createVirement(@RequestBody VirementDTO virementDTO) {
        VirementDTO saved = virementService.saveVirement(virementDTO);
        return ResponseEntity.ok(saved);
    }

    // Update an existing virement
    @PutMapping("/{id}")
    public ResponseEntity<VirementDTO> updateVirement(@PathVariable Long id, @RequestBody VirementDTO virementDTO) {
        VirementDTO updated = virementService.updateVirement(id, virementDTO);
        return ResponseEntity.ok(updated);
    }

    // Delete a virement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVirement(@PathVariable Long id) {
        virementService.deleteVirement(id);
        return ResponseEntity.noContent().build();
    }

    // Get virement by ID
    @GetMapping("/{id}")
    public ResponseEntity<VirementDTO> getVirementById(@PathVariable Long id) {
        return virementService.getVirementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all virements
    @GetMapping
    public ResponseEntity<List<VirementDTO>> getAllVirements() {
        List<VirementDTO> virements = virementService.getAllVirements();
        return ResponseEntity.ok(virements);
    }

    // Get virements by beneficiaire RIB
    @GetMapping("/by-beneficiaire/{rib}")
    public ResponseEntity<List<VirementDTO>> getVirementsByBeneficiaire(@PathVariable String rib) {
        List<VirementDTO> virements = virementService.getVirementsByBeneficiaireRib(rib);
        return ResponseEntity.ok(virements);
    }
}
