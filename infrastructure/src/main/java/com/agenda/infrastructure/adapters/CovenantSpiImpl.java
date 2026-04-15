package com.agenda.infrastructure.adapters;

import com.agenda.domain.covenant.model.CovenantInput;
import com.agenda.domain.covenant.model.dtos.CovenantDto;
import com.agenda.domain.covenant.model.entity.CovenantEntity;
import com.agenda.domain.covenant.port.spi.CovenantSpiPort;
import com.agenda.infrastructure.repositories.associate.persistent.AssociateRepository;
import com.agenda.infrastructure.repositories.covenant.persistent.CovenantRepositoryHandler;
import com.agenda.infrastructure.repositories.covenant.mapper.CovenantMapper;
import com.agenda.infrastructure.rest.covenant.CovenantIntegration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;

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
                .flatMap(getAssociatesCount())
                .map(CovenantMapper::mapToCovenantResponse);
    }
    @Override
    public Mono<CovenantDto> findByCode(Long covenantCode) {
        return repository.findByCode(covenantCode)
                .flatMap(getAssociatesCount())
                .map(CovenantMapper::mapToCovenantResponse);
    }

    @Override
    public Flux<CovenantDto> findByCodeIn(List<Long> covenantId) {
        return repository.findByCodeIn(covenantId)
                .flatMap(getAssociatesCount())
                .mapNotNull(CovenantMapper::mapToCovenantResponse);
    }

    @Override
    public Mono<CovenantDto> save(CovenantInput covenant) {
        return repository.save(CovenantEntity.fromDomain(covenant))
                .map(CovenantEntity::toDomain);
    }

    @Override
    public Mono<CovenantDto> update(String id, CovenantInput covenant) {
        return repository.findById(id)
                .map(entity -> CovenantEntity.fromDomain(entity, covenant))
                .flatMap(repository::save)
                .map(CovenantEntity::toDomain);
    }


    private Function<CovenantEntity, Mono<CovenantEntity>> getAssociatesCount() {
        return covenantEntity -> {
            if (covenantEntity.getAssociatesData() == null ||
                    (covenantEntity.getAssociatesData().associatesUpdatedAt() != null)
                            && ZonedDateTime.now().isAfter(covenantEntity.getAssociatesData().associatesUpdatedAt().plusHours(1))) {
                return associateRepository.countAssociatesByCovenantCode(covenantEntity.getCode())
                        .map(count -> {
                            covenantEntity.setAssociatesData(CovenantEntity.AssociatesData.builder()
                                    .associatesQuantity(count)
                                    .associatesUpdatedAt(ZonedDateTime.now())
                                    .build());
                            return covenantEntity;
                        })
                        .flatMap(repository::save);
            } else {
                return Mono.just(covenantEntity);
            }
        };
    }
}
