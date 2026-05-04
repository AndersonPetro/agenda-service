package com.agendaService.infrastructure.repositories.associate.entity;

import com.agendaService.domain.associate.model.dtos.AssociateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AssociateCovenantResult {
    private String id;
    private String name;
    private String cpf;
    private Long commercialEntityCode;
    private LocalDate birthdate;
    private String email;
    private String cellphone;
    private AssociateEntity.Covenant covenants;

    public AssociateDTO mapToDTO() {
        return AssociateDTO.builder()
                .id(this.id)
                .cpf(this.cpf)
                .name(this.name)
                .commercialEntityCode(this.commercialEntityCode)
                .birthdate(this.birthdate)
                .email(this.covenants.contact().email())
                .cellphone(this.covenants.contact().cellphone())
                .createdAt(LocalDate.from(this.covenants.registration().createdAt()))
                .associateCovenant(AssociateDTO.AssociateCovenant.builder()
                        .isFirstAccess(Boolean.TRUE.equals(this.covenants.isFirstAccess()))
                        .contact(AssociateDTO.AssociateCovenant.Contact.builder()
                                .cellphone(this.covenants.contact().cellphone())
                                .email(this.covenants.contact().email())
                                .build())
                        .covenantDetails(AssociateDTO.AssociateCovenant.CovenantDetails.builder()
                                .code(this.covenants.covenantDetails().code())
                                .name(this.covenants.covenantDetails().name())
                                .subsidiary(this.covenants.covenantDetails().subsidiary())
                                .build())
                        .registration(AssociateDTO.AssociateCovenant.Registration.builder()
                                .createdAt(this.covenants.registration().createdAt())
                                .registrationNumber(this.covenants.registration().registrationNumber())
                                .build())
                        .balance(buildBalance())
                        .card(buildCard())
                        .restrictions(AssociateDTO.AssociateCovenant.Restrictions.builder()
                                .reasons(Optional.ofNullable(this.covenants.restrictions())
                                        .map(restrictions -> restrictions.reasons().stream()
                                                .map(reason -> AssociateDTO.AssociateCovenant.Reason.builder()
                                                        .level(reason.level())
                                                        .createdAt(reason.createdAt())
                                                        .build())
                                                .toList())
                                        .orElse(new ArrayList<>()))
                                .build())
                        .build())
                .build();
    }



    private AssociateDTO.AssociateCovenant.Balance buildBalance() {
        if (Objects.nonNull(this.covenants.balance())) {
            return AssociateDTO.AssociateCovenant.Balance.builder()
                    .spendingLimit(this.covenants.balance().spendingLimit())
                    .totalSpend(this.covenants.balance().totalSpend()).build();
        }
        return null;
    }

    private AssociateDTO.AssociateCovenant.Card buildCard() {
        if (Objects.nonNull(this.covenants.card())) {
            return AssociateDTO.AssociateCovenant.Card.builder()
                    .cardNumber(this.covenants.card().cardNumber())
                    .password(this.covenants.card().encryptedPassword())
                    .createdAt(this.covenants.card().createdAt()).build();
        }
        return null;
    }
}