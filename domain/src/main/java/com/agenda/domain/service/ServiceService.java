package com.agenda.domain.service;

import com.agenda.domain.service.dtos.ServiceDto;
import com.agenda.domain.service.model.ServiceInput;
import com.agenda.domain.service.port.api.ServiceApiPort;
import com.agenda.domain.service.port.spi.ServiceSpiPort;
import com.common.exception.ExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ServiceService implements ServiceApiPort {

    private final ServiceSpiPort serviceSpiPort;

    @Override
    public Mono<ServiceDto> save(ServiceInput input) {
        var dto = ServiceDto.builder()
                .name(input.name())
                .description(input.description())
                .price(input.price())
                .durationMinutes(input.durationMinutes())
                .isActive(true)
                .build();
        return serviceSpiPort.save(dto);
    }

    @Override
    public Mono<ServiceDto> update(String id, ServiceInput input) {
        return serviceSpiPort.findById(id)
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Serviço não encontrado")))
                .flatMap(existing -> {
                    if (input.name() != null) existing.setName(input.name());
                    if (input.description() != null) existing.setDescription(input.description());
                    if (input.price() != null) existing.setPrice(input.price());
                    if (input.durationMinutes() != null) existing.setDurationMinutes(input.durationMinutes());
                    return serviceSpiPort.save(existing);
                });
    }

    @Override
    public Mono<ServiceDto> findById(String id) {
        return serviceSpiPort.findById(id)
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Serviço não encontrado")));
    }

    @Override
    public Flux<ServiceDto> findAll() {
        return serviceSpiPort.findAll();
    }

    @Override
    public Mono<Void> delete(String id) {
        return serviceSpiPort.findById(id)
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Serviço não encontrado")))
                .flatMap(existing -> {
                    existing.setIsActive(false);
                    return serviceSpiPort.save(existing);
                })
                .then();
    }
}

