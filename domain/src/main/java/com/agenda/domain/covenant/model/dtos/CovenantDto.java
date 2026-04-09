package com.agenda.domain.covenant.model.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CovenantDto {
    private String id;
    private Long code;
    private String name;
    private Boolean hasAssociated;
    private Boolean hasMultiplePayments;
    private Boolean isActivePortal;
    private String covenantType;
    @Singular(ignoreNullCollections = true)
    private List<Item> items;
    private Balance balance;
    private Boolean hasOnlinePrescriptions;
    private Long associatesQuantity;
    private List<BenefitDTO> benefits;
    private BigDecimal maxLimitPerAssociate;

    @Builder
    public record BenefitDTO(
            Float discountAmount,
            String discountDescription
    ) {}

    @Builder
    public record Item(
            Long id,
            String category,
            String Uf,
            String drugActiveIngredient,
            String ean,
            String name,
            LocalDate prescriptionDate,
            Integer quantity,
            BigDecimal unitaryValue,
            BigDecimal totalValue,
            BigDecimal discountValue,
            BigDecimal discountPercentage,
            BigDecimal exemptionPercentage
    ) {

    }

    @Builder
    public record Balance(
            BigDecimal spendingLimit,
            BigDecimal totalSpend
    ) {
    }
}
