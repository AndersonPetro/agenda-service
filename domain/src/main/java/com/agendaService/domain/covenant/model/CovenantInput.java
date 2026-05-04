package com.agendaService.domain.covenant.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CovenantInput(
        String id,
        Long code,
        String name,
        CovenantTypeInputEnum covenantType,
        Boolean hasAssociated,
        Boolean hasMultiplePayments,
        Boolean isEnablePortal,
        LocalDateTime importedAt,
        BigDecimal spendingLimit,
        Boolean prescriptionCapture
) {

    @Getter
    public enum CovenantTypeInputEnum {
        DISCOUNT, PAYMENT
    }
}
