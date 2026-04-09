package com.agenda.infrastructure.repositories.associate.entity;

import com.common.enums.AssociateRestrictionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "associate" )
@CompoundIndexes({
        @CompoundIndex(
                name = "unique_covenant_registration",
                def = "{ 'covenants.covenantDetails.code': 1, 'covenants.registration.registrationNumber': 1 }",
                unique = true
        ),
        @CompoundIndex(
                name = "idx_card_number_password",
                def = "{ 'covenants.card.cardNumber': 1, 'covenants.card.encryptedPassword': 1 }",
                background = true
        )
})
public class AssociateEntity {
    @Id
    private String id;
    private String name;
    @Indexed(name = "cpf", unique = true, background = true)
    private String cpf;
    private Long commercialEntityCode;
    private LocalDate birthdate;
    @Builder.Default
    private List<Covenant> covenants = Collections.emptyList();
    private LocalDate createdAt;

    @Builder
    public record Covenant(
            Contact contact,
            CovenantDetails covenantDetails,
            Registration registration,
            Card card,
            Ownership ownership,
            Restrictions restrictions,
            Balance balance,
            Boolean isFirstAccess
    ) {

        @Builder
        public record Contact(
                String email,
                String cellphone
        ) {
        }

    }

    @Builder
    public record CovenantDetails(
            Long code,
            String name,
            String subsidiary
    ) {
    }

    @Builder
    public record Registration(
            String registrationNumber,
            LocalDateTime createdAt
    ) {
    }

    @Builder
    public record Card(
            String cardNumber,
            String encryptedPassword,
            LocalDateTime createdAt
    ) {
    }

    @Builder
    public record Ownership(
            Holder holder,
            HolderDependent dependent
    ) {

    }

    @Builder
    public record HolderDependent(
            Long holderAssociateCode,
            String holderRegistrationNumber
    ) {

    }

    @Builder
    public record Holder(

            List<Dependent> dependents
    ) {
    }

    @Builder
    public record Dependent(
            String name,
            String dependentCode,
            String dependentRegistrationNumber,
            String kinshipType
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
