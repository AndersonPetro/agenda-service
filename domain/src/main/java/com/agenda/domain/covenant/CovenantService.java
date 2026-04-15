package com.agenda.domain.covenant;

import com.agenda.domain.covenant.model.CovenantInput;
import com.agenda.domain.covenant.model.dtos.CovenantDto;
import com.agenda.domain.covenant.port.api.CovenantApiPort;
import com.agenda.domain.covenant.port.spi.CovenantSpiPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class CovenantService implements CovenantApiPort {

    private final CovenantSpiPort covenantSpiPort;

    @Override
    public Mono<CovenantDto> findById(String id) {
        return covenantSpiPort.findById(id);
    }

    @Override
    public Flux<CovenantDto> findByCodeIn(List<Long> covenantId) {
        return covenantSpiPort.findByCodeIn(covenantId);
    }

    @Override
    public Mono<CovenantDto> findByCode(Long covenantId) {
        return covenantSpiPort.findByCode(covenantId);
    }

    @Override
    public Mono<CovenantDto> save(CovenantInput covenant) {
        return covenantSpiPort.save(covenant);
    }


}
