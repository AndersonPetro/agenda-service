package com.agenda.infrastructure.rest.config;

import com.agenda.infrastructure.rest.keycloak.exception.KeycloakExceptionConverter;
import com.agenda.infrastructure.rest.keycloak.exception.WebClientExceptionFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class IntegrationConfig {

    @Bean
    public WebClient keycloakWebClient(Environment environment, KeycloakExceptionConverter keycloakExceptionConverter) {
        return WebClient.builder()
                .baseUrl(environment.getRequiredProperty("restclient.keycloak.baseurl"))
                .defaultHeaders(headers -> headers
                        .add("Authorization", environment.getRequiredProperty("restclient.keycloak.authorization-basic")))
                .filter(new WebClientExceptionFilter(keycloakExceptionConverter))
                .build();
    }

    @Bean
    public WebClient agendaWebClient(IntegrationProperties integrationProperties) {
        return WebClient.builder()
                .baseUrl(integrationProperties.getAgendaBaseUrl())
                .defaultHeaders(headers -> {
                    headers.add(integrationProperties.getTokenName(), integrationProperties.getTokenValue());
                    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .filter(logRequest())
                .filter(logResponse())
                .filter(new WebClientExceptionFilter())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response: rawStatusCode {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
