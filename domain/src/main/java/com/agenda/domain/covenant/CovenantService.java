package com.agenda.domain.covenant;

import com.agenda.domain.covenant.model.CovenantDto;
import com.agenda.domain.covenant.port.api.CovenantApiPort;
import com.agenda.domain.covenant.port.spi.CovenantSpiPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CovenantService implements CovenantApiPort {

    private final CovenantSpiPort covenantSpiPort;

     @Override
    public CovenantDto findById(String id) {
        return covenantSpiPort.findById(id);
    }

}
