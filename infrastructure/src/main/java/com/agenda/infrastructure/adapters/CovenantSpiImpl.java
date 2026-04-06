package com.agenda.infrastructure.adapters;

import com.agenda.domain.covenant.model.CovenantDto;
import com.agenda.domain.covenant.port.spi.CovenantSpiPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class CovenantSpiImpl implements CovenantSpiPort {

    private final CovenantRepositoryHandler repository;
    private final CovenantIntegration covenantIntegration;
    private final AssociateRepository associateRepository;

    @Override
    public Mono<CovenantDto> findById(String id){
        return repository.findById(id)
                .flaMap(getAssocciatesCount())
                .map(CovenantMapper::mapToCovenantResponse);
    }
}
