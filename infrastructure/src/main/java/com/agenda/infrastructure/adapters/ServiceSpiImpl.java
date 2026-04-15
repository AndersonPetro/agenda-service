package com.agenda.infrastructure.adapters;

import com.agenda.domain.service.dtos.ServiceDto;
import com.agenda.domain.service.port.spi.ServiceSpiPort;
import com.agenda.infrastructure.repositories.service.persistent.ServiceEntity;
import com.agenda.infrastructure.repositories.service.persistent.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ServiceSpiImpl implements ServiceSpiPort {

    private final ServiceRepository serviceRepository;

    @Override
    public Mono<ServiceDto> save(ServiceDto serviceDto) {
        return serviceRepository.save(ServiceEntity.fromDomain(serviceDto))
                .map(ServiceEntity::toDomain);
    }

    @Override
    public Mono<ServiceDto> findById(String id) {
        return serviceRepository.findById(id)
                .map(ServiceEntity::toDomain);
    }

    @Override
    public Flux<ServiceDto> findAll() {
        return serviceRepository.findByIsActiveTrue()
                .map(ServiceEntity::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return serviceRepository.deleteById(id);
    }
}

