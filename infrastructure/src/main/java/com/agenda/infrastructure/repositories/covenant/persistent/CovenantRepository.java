package com.agenda.infrastructure.repositories.covenant.persistent;


import com.agenda.domain.covenant.model.entity.CovenantEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CovenantRepository extends ReactiveMongoRepository<CovenantEntity, String> {
    Mono<CovenantEntity> findByCode(Long covenantCode);
    Flux<CovenantEntity> findByCodeIn(List<Long> covenantCode);
}
