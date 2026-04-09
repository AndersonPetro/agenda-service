package com.agenda.domain.associate.model.dtos;


import com.common.enums.AssociateRestrictionEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AssociateDTO {
    private String id;
    private String cpf;
    private String name;
    private LocalDate birthdate;
    private String email;
    private String cellphone;
    @Setter
    private Long commercialEntityCode;
    @Setter
    private AssociateCovenant associateCovenant;
    private LocalDate createdAt;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssociateCovenant {

        private Contact contact;
        private CovenantDetails covenantDetails;
        private Registration registration;
        private Card card;
        private Restrictions restrictions;
        private Balance balance;
        private boolean isFirstAccess;

        @Builder
        public record Contact(
                String email,
                String cellphone
        ){}

        @Builder
        public record CovenantDetails(
                Long code,
                String name,
                String subsidiary
        ) {
        }

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class Registration {
            private String registrationNumber;
            private LocalDateTime createdAt;
        }

        @Builder(toBuilder = true)
        public record Card(
                String cardNumber,
                LocalDateTime createdAt,
                String password
        ) {
        }

        @Builder
        public record Restrictions(
                List<Reason> reasons
        ) {

        }

        @Builder
        public record Reason(
                AssociateRestrictionEnum level,
                LocalDateTime createdAt
        ) {
        }

        @Builder
        public record Balance(
                BigDecimal spendingLimit,
                BigDecimal totalSpend,
                List<TransactionHistory> transactionHistory
        ) {

        }

        @Builder
        public record TransactionHistory(
                LocalDateTime periodStartDate,
                LocalDateTime periodEndDate,
                BigDecimal totalSpend
        ) {
        }
    }
}
