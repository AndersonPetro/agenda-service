package com.agendaService.domain.service.port.api;

import com.agendaService.domain.service.dtos.ServiceDto;
import com.agendaService.domain.service.model.ServiceInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceApiPort {
    Mono<ServiceDto> save(ServiceInput input);
    Mono<ServiceDto> update(String id, ServiceInput input);
    Mono<ServiceDto> findById(String id);
    Flux<ServiceDto> findAll();
    Mono<Void> delete(String id);
}

