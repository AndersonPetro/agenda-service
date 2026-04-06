package com.agenda.domain.covenant.port.api;

import com.agenda.domain.covenant.model.CovenantDto;
import reactor.core.publisher.Flux;

public interface CovenantApiPort {
    Flux<CovenantDto> findByCodeIn(String id);
}
