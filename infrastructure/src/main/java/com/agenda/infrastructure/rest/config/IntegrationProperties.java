package com.agenda.infrastructure.rest.config;

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

    @Value("${restclient.dimed.baseurl}")
    private String dimedBaseUrl;

    @Value("${restclient.dimed.token.header}")
    private String tokenName;

    @Value("${restclient.dimed.token.value}")
    private String tokenValue;
}
