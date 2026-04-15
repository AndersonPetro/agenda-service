package com.agenda.domain.service.port.api;

import com.agenda.domain.service.dtos.ServiceDto;
import com.agenda.domain.service.model.ServiceInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceApiPort {
    Mono<ServiceDto> save(ServiceInput input);
    Mono<ServiceDto> update(String id, ServiceInput input);
    Mono<ServiceDto> findById(String id);
    Flux<ServiceDto> findAll();
    Mono<Void> delete(String id);
}

