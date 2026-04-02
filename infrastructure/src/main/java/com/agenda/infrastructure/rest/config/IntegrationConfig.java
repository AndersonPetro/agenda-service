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
                .baseUrl(environment.getRequiredProperty("restclient.keycloak.baseurl")
                        .concat("/auth"))
                .defaultHeaders(headers -> headers
                        .add("Authorization", environment.getRequiredProperty("restclient.keycloak.authorization-basic")))
                .filter(new WebClientExceptionFilter(keycloakExceptionConverter))
                .build();
    }

    @Bean
    public WebClient recaptchaWebClient(Environment environment) {
        return WebClient.builder()
                .baseUrl(environment.getRequiredProperty("restclient.recaptcha.baseurl"))
                .defaultHeaders(httpHeaders -> httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .filter(new WebClientExceptionFilter())
                .build();
    }

    @Bean
    public WebClient dimedWebClient(IntegrationProperties integrationProperties) {
        return WebClient.builder()
                .baseUrl(integrationProperties.getDimedBaseUrl())
                .defaultHeaders(headers -> getHeaders(
                                integrationProperties.getTokenName(),
                                integrationProperties.getTokenValue(),
                                headers
                        )
                )
                .filter(logRequest())
                .filter(logResponse())
                .filter(new WebClientExceptionFilter())
                .build();
    }

    @Bean
    public WebClient localWebClient(IntegrationProperties integrationProperties) {
        return WebClient.builder()
                .baseUrl("http://localhost:9000")
                .defaultHeaders(headers -> getHeaders(
                                integrationProperties.getTokenName(),
                                integrationProperties.getTokenValue(),
                                headers
                        )
                )
                .filter(logRequest())
                .filter(logResponse())
                .filter(new WebClientExceptionFilter())
                .build();
    }

    private void getHeaders(String tokenName, String tokenValue, HttpHeaders httpHeaders) {
        httpHeaders.add(tokenName, tokenValue);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response: {}", clientResponse.headers().asHttpHeaders().get("property-header"));
            log.info("Response: {}", clientResponse.headers());
            log.info("Response: rawStatusCode {}", clientResponse.rawStatusCode());
            return Mono.just(clientResponse);
        });
    }
}
