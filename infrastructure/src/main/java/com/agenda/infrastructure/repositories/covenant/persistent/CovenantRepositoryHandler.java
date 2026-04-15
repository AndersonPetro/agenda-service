package com.agenda.infrastructure.repositories.covenant.persistent;


import com.agenda.domain.covenant.model.entity.CovenantEntity;
import com.common.exception.AgendaHttpException;
import com.common.gmt.query.QueryFilters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
public class CovenantRepositoryHandler {

    private static final List<String> AVAILABLE_SEARCH_FILTERS = Arrays.asList(
            "code",
            "name",
            "covenantType",
            "isActivePortal"
    );
    private final ReactiveMongoTemplate mongoTemplate;
    private final CovenantRepository repository;


    public Mono<CovenantEntity> findById(String id) {
        return repository.findById(id);
    }

    public Mono<CovenantEntity> findByCode(Long covenantId) {
        return repository.findByCode(covenantId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(AgendaHttpException.withHttp404().withMessage("Convênio não encontrado").build())));
    }

    public Flux<CovenantEntity> findByCodeIn(List<Long> covenantId) {
        return repository.findByCodeIn(covenantId);
    }

    public Mono<CovenantEntity> save(CovenantEntity covenant) {
        log.info("Insert covenant --> {}", covenant);
        return repository.save(covenant);
    }


    public Flux<CovenantEntity> findAllWithFilters(PageRequest pageRequest, Map<String, String> filters) {
        return mongoTemplate.find(buildQueryWithFilters(pageRequest, filters), CovenantEntity.class);
    }


    public Mono<Long> countAllWithFilters(Map<String, String> filters) {
        return mongoTemplate.count(buildQueryWithFilters(null, filters), CovenantEntity.class);
    }


    private Query buildQueryWithFilters(Pageable pageable, Map<String, String> filters) {
        final Query query = new Query();
        final List<Criteria> criteria = new ArrayList<>();

        QueryFilters.collectFiltersToMap(filters, AVAILABLE_SEARCH_FILTERS)
                .forEach((key, value) -> {
                    if ("name".equals(key)) {
                        var nameSearchLike = ".*" + value.toUpperCase() + ".*";
                        criteria.add(Criteria.where(key).regex(nameSearchLike));
                    } else if ("isActivePortal".equals(key)) {
                        criteria.add(Criteria
                                .where(key)
                                .is(Boolean.parseBoolean(value)));
                    } else {
                        if (key.equals("code")) {
                            criteria.add(Criteria.where(key).is(Long.parseLong(value)));
                        } else {
                            criteria.add(Criteria.where(key).is(value));
                        }
                    }
                });

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria));
        }
        if (pageable != null) {
            query.with(pageable);
        }
        return query;
    }
}