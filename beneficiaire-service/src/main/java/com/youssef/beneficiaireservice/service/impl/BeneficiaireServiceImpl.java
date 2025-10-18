package com.youssef.beneficiaireservice.service.impl;

import com.youssef.beneficiaireservice.dto.BeneficiaireRequestDTO;
import com.youssef.beneficiaireservice.dto.BeneficiaireResponseDTO;
import com.youssef.beneficiaireservice.dto.BeneficiaireUpdateDTO;
import com.youssef.beneficiaireservice.entities.Beneficiaire;
import com.youssef.beneficiaireservice.enums.TypeBeneficiaire;

import com.youssef.beneficiaireservice.mapper.BeneficiaireMapper;
import com.youssef.beneficiaireservice.repositories.BeneficiaireRepo;
import com.youssef.beneficiaireservice.service.IBeneficiaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BeneficiaireServiceImpl implements IBeneficiaireService {

    private final BeneficiaireRepo beneficiaireRepository;
    private final BeneficiaireMapper beneficiaireMapper;

    @Override
    public BeneficiaireResponseDTO create(BeneficiaireRequestDTO dto) {
        Beneficiaire entity = beneficiaireMapper.toEntity(dto);
        entity.setActif(true);
        Beneficiaire saved = beneficiaireRepository.save(entity);
        return beneficiaireMapper.toResponseDTO(saved);
    }

    @Override
    public BeneficiaireResponseDTO getById(Long id) {
        Beneficiaire entity = beneficiaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bénéficiaire non trouvé avec id: " + id));
        return beneficiaireMapper.toResponseDTO(entity);
    }

    @Override
    public BeneficiaireResponseDTO getByRib(String rib) {
        Beneficiaire entity = beneficiaireRepository.findByRib(rib)
                .orElseThrow(() -> new RuntimeException("Bénéficiaire non trouvé avec RIB: " + rib));
        return beneficiaireMapper.toResponseDTO(entity);
    }

    @Override
    public List<BeneficiaireResponseDTO> getAll() {
        return beneficiaireRepository.findAll()
                .stream()
                .map(beneficiaireMapper::toResponseDTO)
                .toList();
    }

    @Override
    public BeneficiaireResponseDTO update(Long id, BeneficiaireUpdateDTO dto) {
        Beneficiaire entity = beneficiaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bénéficiaire non trouvé avec id: " + id));
        beneficiaireMapper.updateBeneficiaireFromDTO(dto, entity);
        Beneficiaire updated = beneficiaireRepository.save(entity);
        return beneficiaireMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!beneficiaireRepository.existsById(id)) {
            throw new RuntimeException("Bénéficiaire non trouvé avec id: " + id);
        }
        beneficiaireRepository.deleteById(id);
    }

    @Override
    public List<BeneficiaireResponseDTO> getByType(TypeBeneficiaire type) {
        return beneficiaireRepository.findByType(type)
                .stream()
                .map(beneficiaireMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BeneficiaireResponseDTO> getActifs() {
        return beneficiaireRepository.findByActifTrue()
                .stream()
                .map(beneficiaireMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BeneficiaireResponseDTO> getInactifs() {
        return beneficiaireRepository.findByActifFalse()
                .stream()
                .map(beneficiaireMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BeneficiaireResponseDTO> search(String keyword) {
        return beneficiaireRepository.searchByNomOrPrenom(keyword)
                .stream()
                .map(beneficiaireMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<BeneficiaireResponseDTO> searchWithFilters(TypeBeneficiaire type, Boolean actif, String keyword) {
        return beneficiaireRepository.searchWithFilters(type, actif, keyword)
                .stream()
                .map(beneficiaireMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void deactivate(Long id) {
        Beneficiaire entity = beneficiaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bénéficiaire non trouvé avec id: " + id));
        entity.setActif(false);
        beneficiaireRepository.save(entity);
    }

    @Override
    public void activate(Long id) {
        Beneficiaire entity = beneficiaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bénéficiaire non trouvé avec id: " + id));
        entity.setActif(true);
        beneficiaireRepository.save(entity);
    }

    @Override
    public boolean existsByRib(String rib) {
        return beneficiaireRepository.existsByRib(rib);
    }

    @Override
    public boolean existsById(Long id) {
        return beneficiaireRepository.existsById(id);
    }

    @Override
    public long count() {
        return beneficiaireRepository.count();
    }

    @Override
    public long countByType(TypeBeneficiaire type) {
        return beneficiaireRepository.countByType(type);
    }

    @Override
    public long countActifs() {
        return beneficiaireRepository.countByActifTrue();
    }
}
