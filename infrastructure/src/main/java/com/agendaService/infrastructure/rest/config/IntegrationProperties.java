package com.agendaService.infrastructure.rest.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationProperties {

    @Value("${restclient.agenda-service.baseurl}")
    private String agendaBaseUrl;

    @Value("${restclient.agenda-service.token.header}")
    private String tokenName;

    @Value("${restclient.agenda-service.token.value}")
    private String tokenValue;
}
