package com.agendaService.infrastructure.repositories.user.persistent;

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
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
public class UserRepositoryHandler {
    private final ReactiveMongoTemplate mongoTemplate;


    public Flux<UserEntity> findAllWithFilters(PageRequest pageRequest, Map<String, String> filters) {
        return mongoTemplate.find(buildQueryWithFilters(pageRequest, filters), UserEntity.class);
    }

    public Mono<Long> countAllWithFilters(Map<String, String> filters) {
        return mongoTemplate.count(buildQueryWithFilters(null, filters), UserEntity.class);
    }

    private Query buildQueryWithFilters(Pageable pageable, Map<String, String> filters) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        if (filters.containsKey("isActive"))
            criteria.add(Criteria.where("isActive").is(Boolean.parseBoolean(filters.get("isActive"))));

        if (filters.containsKey("name"))
            criteria.add(Criteria.where("name").regex(filters.get("name"), "i"));

        if (filters.containsKey("covenants")) {
            final var covenant = filters.get("covenants");
            if (covenant.matches("[0-9]+"))
                criteria.add(Criteria.where("covenants.code").is(Long.valueOf(covenant)));
            else criteria.add(Criteria.where("covenants.name").regex(covenant, "i"));
        }

        if (filters.containsKey("covenantId"))
            criteria.add(Criteria.where("covenants.id").is(filters.get("covenantId")));

        else if (filters.containsKey("covenantCode"))
            criteria.add(Criteria.where("covenants.code").is(filters.get("covenantCode")));

        if (!criteria.isEmpty()) query.addCriteria(new Criteria().andOperator(criteria));
        if (pageable != null) query.with(pageable);
        return query;

    }
}