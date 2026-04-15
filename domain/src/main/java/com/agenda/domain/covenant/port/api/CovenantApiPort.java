package com.agenda.domain.covenant.port.api;

import com.agenda.domain.covenant.model.CovenantInput;
import com.agenda.domain.covenant.model.dtos.CovenantDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CovenantApiPort {
    Mono<CovenantDto> findById(String id);
    Flux<CovenantDto> findByCodeIn(List<Long> covenantId);
    Mono<CovenantDto> findByCode(Long covenantId);
    Mono<CovenantDto> save(CovenantInput covenant);
}
