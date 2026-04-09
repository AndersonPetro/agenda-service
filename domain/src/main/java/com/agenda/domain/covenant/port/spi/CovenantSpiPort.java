package com.agenda.domain.covenant.port.spi;

import com.agenda.domain.covenant.model.CovenantInput;
import com.agenda.domain.covenant.model.dtos.CovenantDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CovenantSpiPort {

    Flux<CovenantDto> findByCodeIn(List<Long> covenantId);
    Mono<CovenantDto> findById(String id);
    Mono<CovenantDto> findByCode(Long covenantCode);
    Mono<CovenantDto> save(CovenantInput covenant);
    Mono<CovenantDto> update(String id, CovenantInput covenant);
}
