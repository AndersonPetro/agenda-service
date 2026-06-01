package com.agendaService.infrastructure.adapters;

import com.agendaService.domain.auth.port.spi.AuthSpiPort;
import com.agendaService.infrastructure.rest.keycloak.KeycloakIntegration;
import com.agendaService.infrastructure.rest.keycloak.authenticate.request.CredentialRequest;
import com.agendaService.infrastructure.rest.keycloak.authenticate.request.KeycloakUserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class AuthSpiImpl implements AuthSpiPort {

    private final KeycloakIntegration keycloakIntegration;

    @Override
    public Mono<Map<String, Object>> authenticateUser(String email, String password) {
        return Mono.fromCallable(() -> keycloakIntegration.authenticateUser(email, password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> createUser(String firstName, String lastName, String email, String password) {
        var request = KeycloakUserRequest.builder()
                .username(email)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .enabled(true)
                .emailVerified(true)
                .credentials(List.of(
                        CredentialRequest.builder()
                                .type("password")
                                .value(password)
                                .temporary(false)
                                .build()
                ))
                .build();

        return Mono.fromCallable(() -> keycloakIntegration.createUser(request))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
