package com.agendaService.infrastructure.repositories.service.persistent;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ServiceRepository extends ReactiveMongoRepository<ServiceEntity, String> {
    Flux<ServiceEntity> findByIsActiveTrue();
}

