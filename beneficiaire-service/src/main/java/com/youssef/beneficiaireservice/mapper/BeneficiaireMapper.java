package com.youssef.beneficiaireservice.mapper;

import com.youssef.beneficiaireservice.dto.*;
import com.youssef.beneficiaireservice.entities.Beneficiaire;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeneficiaireMapper {

    // ======= Entity ↔ Request DTO =======
    Beneficiaire toEntity(BeneficiaireRequestDTO dto);

    BeneficiaireResponseDTO toResponseDTO(Beneficiaire entity);

    // ======= Update DTO (partial update) =======
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBeneficiaireFromDTO(BeneficiaireUpdateDTO dto, @MappingTarget Beneficiaire entity);

    // ======= Entity ↔ Summary DTO =======
    @Mapping(target = "nomComplet", expression = "java(entity.getNom() + \" \" + entity.getPrenom())")
    BeneficiaireSummaryDTO toSummaryDTO(Beneficiaire entity);

    // ======= Lists =======
    List<BeneficiaireResponseDTO> toResponseDTOList(List<Beneficiaire> entities);

    List<BeneficiaireSummaryDTO> toSummaryDTOList(List<Beneficiaire> entities);
}
