package com.agenda.infrastructure.repositories.associate.persistent;

import com.agenda.infrastructure.repositories.associate.entity.AssociateCovenantResult;
import com.agenda.infrastructure.repositories.associate.entity.AssociateEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
@AllArgsConstructor
public class AssociateRepositoryHandlerImpl implements AssociateRepositoryHandler {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<AssociateCovenantResult> findAssociateByCpfAndCovenantCode(String cpf, Long covenantCode) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("cpf").is(cpf)),
                Aggregation.unwind("covenants"),
                Aggregation.match(Criteria.where("covenants.covenantDetails.code").is(covenantCode)),
                Aggregation.project()
                        .and("_id").as("id")
                        .and("name").as("name")
                        .and("cpf").as("cpf")
                        .and("commercialEntityCode").as("commercialEntityCode")
                        .and("birthdate").as("birthdate")
                        .and("covenants").as("covenants")
        );
        return mongoTemplate.aggregate(aggregation, "associate", AssociateCovenantResult.class).next();
    }

    @Override
    public Mono<AssociateCovenantResult> findAssociateByRegistrationAndCovenantCode(String registration, Long covenantCode) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.unwind("covenants"),
                Aggregation.match(new Criteria().andOperator(
                        Criteria.where("covenants.covenantDetails.code").is(covenantCode),
                        Criteria.where("covenants.registration.registrationNumber").is(registration)
                )),
                Aggregation.project()
                        .and("_id").as("id")
                        .and("name").as("name")
                        .and("cpf").as("cpf")
                        .and("commercialEntityCode").as("commercialEntityCode")
                        .and("birthdate").as("birthdate")
                        .and("covenants").as("covenants")
        );
        return mongoTemplate.aggregate(aggregation, "associate", AssociateCovenantResult.class).next();
    }

    @Override
    public Mono<Void> updateAssociate(String cpf, Map<String, Object> params) {
        Query query = new Query(Criteria.where("cpf").is(cpf));
        Update update = new Update();
        params.forEach(update::set);
        return mongoTemplate.updateFirst(query, update, AssociateEntity.class).then();
    }

    @Override
    public Mono<AssociateCovenantResult> save(AssociateCovenantResult associateCovenantResult) {
        return Mono.just(associateCovenantResult);
    }

    @Override
    public Flux<AssociateCovenantResult> findWithDependentsByRegistrationAndCovenantCode(String registration, Long covenantCode) {
        var aggregation = Aggregation.newAggregation(
                Aggregation.unwind("covenants"),
                Aggregation.match(new Criteria().andOperator(
                        Criteria.where("covenants.covenantDetails.code").is(covenantCode),
                        Criteria.where("covenants.registration.registrationNumber").is(registration)
                )),
                Aggregation.project()
                        .and("_id").as("id")
                        .and("name").as("name")
                        .and("cpf").as("cpf")
                        .and("commercialEntityCode").as("commercialEntityCode")
                        .and("birthdate").as("birthdate")
                        .and("covenants").as("covenants")
        );
        return mongoTemplate.aggregate(aggregation, "associate", AssociateCovenantResult.class);
    }

    @Override
    public Mono<Long> countAssociatesByCovenantCode(Long covenantCode) {
        Query query = new Query(Criteria.where("covenants.covenantDetails.code").is(covenantCode));
        return mongoTemplate.count(query, AssociateEntity.class);
    }
}

