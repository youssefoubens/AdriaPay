
package com.youssef.virementservice.services.impl;

import com.youssef.virementservice.client.BeneficiaireClient;
import com.youssef.virementservice.dto.VirementDTO;
import com.youssef.virementservice.entities.Virement;
import com.youssef.virementservice.mapper.VirementMapper;
import com.youssef.virementservice.models.Beneficiaire;
import com.youssef.virementservice.repositories.VirementRepo;
import com.youssef.virementservice.services.VirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VirementServiceImpl implements VirementService {

    private final VirementRepo virementRepository;
    private final VirementMapper virementMapper;
    private final BeneficiaireClient beneficiaireClient;

    @Override
    public VirementDTO saveVirement(VirementDTO virementDTO) {
        // Fetch Beneficiaire from beneficiaire-service
        Beneficiaire beneficiaire = beneficiaireClient.getBeneficiaireByRib(virementDTO.getBeneficiaire().getRib());
        virementDTO.setBeneficiaire(beneficiaire);

        Virement virement = virementMapper.toEntity(virementDTO);

        // Ensure beneficiaireRib is set from the beneficiaire object
        if (beneficiaire != null && beneficiaire.getRib() != null) {
            virement.setBeneficiaireRib(beneficiaire.getRib());
        } else {
            throw new RuntimeException("Beneficiaire RIB cannot be null");
        }

        Virement saved = virementRepository.save(virement);

        VirementDTO dto = virementMapper.toDTO(saved);
        dto.setBeneficiaire(beneficiaire); // set because entity has @Transient
        return dto;
    }

    @Override
    public VirementDTO updateVirement(Long id, VirementDTO virementDTO) {
        Virement existing = virementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Virement not found with id: " + id));

        existing.setMontant(virementDTO.getMontant());
        existing.setDescription(virementDTO.getDescription());
        existing.setDateVirement(virementDTO.getDateVirement());
        existing.setType(virementDTO.getType());
        existing.setRibSource(virementDTO.getRibSource());
        existing.setActif(virementDTO.getActif());

        // Update beneficiaireRib if beneficiaire is provided
        if (virementDTO.getBeneficiaire() != null && virementDTO.getBeneficiaire().getRib() != null) {
            existing.setBeneficiaireRib(virementDTO.getBeneficiaire().getRib());
        }

        Virement updated = virementRepository.save(existing);

        VirementDTO dto = virementMapper.toDTO(updated);
        dto.setBeneficiaire(virementDTO.getBeneficiaire()); // set DTO beneficiaire
        return dto;
    }

    @Override
    public void deleteVirement(Long id) {
        virementRepository.deleteById(id);
    }

    @Override
    public Optional<VirementDTO> getVirementById(Long id) {
        return virementRepository.findById(id)
                .map(v -> {
                    VirementDTO dto = virementMapper.toDTO(v);
                    dto.setBeneficiaire(v.getBeneficiaire()); // set transient field
                    return dto;
                });
    }

    @Override
    public List<VirementDTO> getAllVirements() {
        return virementRepository.findAll()
                .stream()
                .map(v -> {
                    VirementDTO dto = virementMapper.toDTO(v);
                    dto.setBeneficiaire(v.getBeneficiaire());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override

    public List<VirementDTO> getVirementsByBeneficiaireRib(String rib) {
        return virementRepository.findByBeneficiaireRib(rib)
                .stream()
                .map(v -> {
                    VirementDTO dto = virementMapper.toDTO(v);
                    dto.setBeneficiaire(v.getBeneficiaire());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}