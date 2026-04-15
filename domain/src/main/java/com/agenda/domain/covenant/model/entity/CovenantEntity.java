package com.agenda.domain.covenant.model.entity;

import com.agenda.domain.covenant.model.CovenantInput;
import com.agenda.domain.covenant.model.dtos.CovenantDto;
import com.common.enums.CovenantTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "covenant")
public class CovenantEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private Long code;
    @Indexed
    private String name;
    @Builder.Default
    private Boolean hasAssociated = false;
    @Builder.Default
    private Boolean hasMultiplePayments = false;
    @Builder.Default
    private Boolean isActivePortal = false;
    private CovenantTypeEnum covenantType;
    private LocalDateTime deleteAt;
    private LocalDateTime updateAt;
    private LocalDateTime importedAt;
    private EntityBalance balance;
    private AssociatesData associatesData;
    private List<Benefit> benefits;

    @Builder
    public record AssociatesData (
            Long associatesQuantity,
            ZonedDateTime associatesUpdatedAt
    ) {}

    public CovenantDto toDomain() {
        return CovenantDto.builder()
                .id(id)
                .code(code)
                .name(name)
                .hasAssociated(hasAssociated)
                .hasMultiplePayments(hasMultiplePayments)
                .isActivePortal(isActivePortal)
                .covenantType(covenantType.name())
                .balance(CovenantDto.Balance.builder()
                        .spendingLimit(Optional.ofNullable(balance).map(EntityBalance::getSpendingLimit).orElse(null))
                        .build())
                .build();
    }

    public static CovenantEntity fromDomain(CovenantInput covenantInput) {
        return CovenantEntity.builder()
                .code(covenantInput.code())
                .name(covenantInput.name().toUpperCase())
                .hasAssociated(Optional.ofNullable(covenantInput.hasAssociated()).orElse(Boolean.FALSE))
                .hasMultiplePayments(Optional.ofNullable(covenantInput.hasMultiplePayments()).orElse(Boolean.FALSE))
                .isActivePortal(Optional.ofNullable(covenantInput.isEnablePortal()).orElse(Boolean.FALSE))
                .covenantType(
                        Optional.ofNullable(covenantInput.covenantType())
                                .map(Enum::name)
                                .map(CovenantTypeEnum::valueOf)
                                .orElse(CovenantTypeEnum.DISCOUNT)
                )
                .importedAt(LocalDateTime.now())
                .balance(
                        EntityBalance.builder()
                                .spendingLimit(covenantInput.spendingLimit())
                                .build()
                )
                .build();
    }

    public static CovenantEntity fromDomain(CovenantEntity existing, CovenantInput covenantInput) {
        existing.setName(Optional.ofNullable(covenantInput.name()).map(String::toUpperCase).orElse(existing.getName()));
        existing.setHasAssociated(Optional.ofNullable(covenantInput.hasAssociated()).orElse(existing.getHasAssociated()));
        existing.setHasMultiplePayments(Optional.ofNullable(covenantInput.hasMultiplePayments()).orElse(existing.getHasMultiplePayments()));
        existing.setIsActivePortal(Optional.ofNullable(covenantInput.isEnablePortal()).orElse(existing.getIsActivePortal()));
        existing.setCovenantType(
                Optional.ofNullable(covenantInput.covenantType())
                        .map(Enum::name)
                        .map(CovenantTypeEnum::valueOf)
                        .orElse(existing.getCovenantType())
        );
        existing.setUpdateAt(LocalDateTime.now());
        if (covenantInput.spendingLimit() != null) {
            existing.setBalance(EntityBalance.builder().spendingLimit(covenantInput.spendingLimit()).build());
        }
        return existing;
    }
}
