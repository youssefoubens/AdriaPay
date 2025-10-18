package com.youssef.virementservice.entities;

import com.youssef.virementservice.enums.TypeVirement;

import com.youssef.virementservice.models.Beneficiaire;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "virements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Virement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation with Beneficiaire
    @Transient
    private Beneficiaire beneficiaire;

    @Column(nullable = false, length = 24)
    private String ribSource;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateVirement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeVirement type;

    @Column(nullable = false)
    private Boolean actif = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
