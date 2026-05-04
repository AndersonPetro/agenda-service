package com.agendaService.infrastructure.rest.covenant;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CovenantIntegration {
    private final WebClient webClient;

    public CovenantIntegration(@Qualifier("agendaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }
}
