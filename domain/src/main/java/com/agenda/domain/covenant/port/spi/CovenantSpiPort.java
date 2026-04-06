package com.agenda.domain.covenant.port.spi;

import com.agenda.domain.covenant.model.CovenantDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CovenantSpiPort {

    Flux<CovenantDto> findByCodeIn(List<Long> covenantId);
    Mono<CovenantDto> findById(String id);
}
