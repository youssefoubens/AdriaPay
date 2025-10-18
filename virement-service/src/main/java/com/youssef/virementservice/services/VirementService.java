package com.youssef.virementservice.services;

import com.youssef.virementservice.dto.VirementDTO;

import java.util.List;
import java.util.Optional;

public interface VirementService {

    VirementDTO saveVirement(VirementDTO virementDTO);

    VirementDTO updateVirement(Long id, VirementDTO virementDTO);

    void deleteVirement(Long id);

    Optional<VirementDTO> getVirementById(Long id);

    List<VirementDTO> getAllVirements();

    List<VirementDTO> getVirementsByBeneficiaireRib(String rib);

}
