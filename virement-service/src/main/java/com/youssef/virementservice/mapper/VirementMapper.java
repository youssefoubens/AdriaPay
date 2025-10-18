package com.youssef.virementservice.mapper;

import com.youssef.virementservice.dto.VirementDTO;
import com.youssef.virementservice.entities.Virement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VirementMapper {

    VirementMapper INSTANCE = Mappers.getMapper(VirementMapper.class);

    @Mapping(target = "beneficiaire", source = "beneficiaire")
    VirementDTO toDTO(Virement virement);

    @Mapping(target = "beneficiaire", ignore = true) // Beneficiaire is transient, handled separately
    Virement toEntity(VirementDTO dto);
}
