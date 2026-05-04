package com.agendaService.domain.service.port.spi;

import com.agendaService.domain.service.dtos.ServiceDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServiceSpiPort {
    Mono<ServiceDto> save(ServiceDto serviceDto);
    Mono<ServiceDto> findById(String id);
    Flux<ServiceDto> findAll();
    Mono<Void> deleteById(String id);
}

