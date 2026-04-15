package com.agenda.infrastructure.adapters;

import com.agenda.domain.auth.port.spi.AuthSpiPort;
import com.agenda.infrastructure.rest.keycloak.KeycloakIntegration;
import com.agenda.infrastructure.rest.keycloak.authenticate.request.CredentialRequest;
import com.agenda.infrastructure.rest.keycloak.authenticate.request.KeycloakUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AuthSpiImpl implements AuthSpiPort {

    private final KeycloakIntegration keycloakIntegration;
    private final WebClient keycloakWebClient;
    private final Environment environment;

    public AuthSpiImpl(KeycloakIntegration keycloakIntegration,
                       @Qualifier("keycloakWebClient") WebClient keycloakWebClient,
                       Environment environment) {
        this.keycloakIntegration = keycloakIntegration;
        this.keycloakWebClient = keycloakWebClient;
        this.environment = environment;
    }

    private static final String REALM = "restclient.keycloak.realm";

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> authenticateUser(String email, String password) {
        return keycloakWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/realms/{realm}/protocol/openid-connect/token")
                        .build(environment.getRequiredProperty(REALM)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("username", email)
                        .with("password", password))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response);
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

        return keycloakIntegration.createUser(request);
    }
}

