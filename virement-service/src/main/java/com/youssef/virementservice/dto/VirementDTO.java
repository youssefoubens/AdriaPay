package com.youssef.virementservice.dto;

import com.youssef.virementservice.enums.TypeVirement;
import com.youssef.virementservice.models.Beneficiaire;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirementDTO {

    private Long id;

    private Beneficiaire beneficiaire; // Will be populated via Feign

    private String ribSource;

    private BigDecimal montant;

    private String description;

    private LocalDateTime dateVirement;

    private TypeVirement type;

    private Boolean actif;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
