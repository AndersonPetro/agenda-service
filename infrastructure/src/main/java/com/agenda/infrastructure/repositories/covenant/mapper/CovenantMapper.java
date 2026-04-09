package com.agenda.infrastructure.repositories.covenant.mapper;


import com.agenda.domain.covenant.model.dtos.CovenantDto;
import com.agenda.domain.covenant.model.entity.CovenantEntity;
import com.common.enums.CovenantTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CovenantMapper {

    public static CovenantDto mapToCovenantResponse(CovenantEntity covenantEntity) {
        return CovenantDto.builder()
                .id(covenantEntity.getId())
                .code(covenantEntity.getCode())
                .name(covenantEntity.getName())
                .hasAssociated(covenantEntity.getHasAssociated())
                .hasMultiplePayments(covenantEntity.getHasMultiplePayments())
                .isActivePortal(covenantEntity.getIsActivePortal())
                .covenantType(covenantEntity.getCovenantType().name())
                .balance(covenantEntity.getCovenantType().equals(CovenantTypeEnum.PAYMENT) && Objects.nonNull(covenantEntity.getBalance()) ?
                        CovenantDto.Balance.builder()
                                .spendingLimit(covenantEntity.getBalance().getSpendingLimit())
                                .build() : null)
                .associatesQuantity(Optional.ofNullable(covenantEntity.getAssociatesData()).map(CovenantEntity.AssociatesData::associatesQuantity).orElse(null))
                .benefits(Optional.ofNullable(covenantEntity.getBenefits()).map(benefits -> benefits.stream()
                                .map(benefit -> CovenantDto.BenefitDTO.builder()
                                        .discountAmount(benefit.getDiscountAmount())
                                        .discountDescription(benefit.getDiscountDescription())
                                        .build()).toList())
                        .orElse(List.of()))
                .build();
    }
}