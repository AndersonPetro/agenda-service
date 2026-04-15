package com.agenda.infrastructure.rest.keycloak;

import com.agenda.infrastructure.rest.keycloak.authenticate.request.KeycloakUserRequest;
import com.agenda.infrastructure.rest.keycloak.authenticate.response.KeycloakAuthenticationResponse;
import com.agenda.infrastructure.rest.keycloak.authenticate.response.KeycloakUserResponse;
import com.common.exception.AgendaHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Service
@Slf4j
public class KeycloakIntegration {

    private final WebClient keycloakWebClient;
    private static final String REALM = "restclient.keycloak.realm";
    private final Environment environment;

    public KeycloakIntegration(@Qualifier("keycloakWebClient") WebClient keycloakWebClient, Environment environment) {
        this.keycloakWebClient = keycloakWebClient;
        this.environment = environment;
    }

    public Mono<KeycloakAuthenticationResponse> authenticateClientCredentials() {
        return keycloakWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/realms/{realm}/protocol/openid-connect/token")
                        .build(environment.getRequiredProperty(REALM)))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(KeycloakAuthenticationResponse.class);
    }

    public Mono<KeycloakUserResponse> findUserWithCriteria(String username, String email) {
        return authenticateClientCredentials()
                .flatMap(authentication -> keycloakWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/admin/realms/{realm}/users")
                                .queryParamIfPresent("username", Optional.ofNullable(username))
                                .queryParamIfPresent("email", Optional.ofNullable(email))
                                .build(environment.getRequiredProperty(REALM))
                        )
                        .header("Authorization", "Bearer " + authentication.getAccessToken())
                        .retrieve()
                        .bodyToFlux(KeycloakUserResponse.class)
                        .filter(user -> user.getUsername().equals(username) || user.getEmail().equals(email))
                        .collectList()
                        .flatMap(users -> {
                            if (users.size() == 1) {
                                return Mono.just(users.get(0));
                            } else if (users.isEmpty()) {
                                log.info("Nenhum usuário encontrado para os filtros username={}, email={}", username, email);
                                return Mono.empty();
                            } else {
                                log.warn("Mais de um usuário retornado para os filtros username={}, email={}. Cancelando operação.", username, email);
                                throw AgendaHttpException.withHttp412()
                                        .withMessage("Não é possível identificar o usuário")
                                        .build();
                            }
                        }));
    }

    public Mono<String> createUser(KeycloakUserRequest keycloakUserRequest) {
        return authenticateClientCredentials()
                .flatMap(authentication -> keycloakWebClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/admin/realms/{realm}/users")
                                .build(environment.getRequiredProperty(REALM)))
                        .header("Authorization", "Bearer " + authentication.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(keycloakUserRequest)
                        .retrieve()
                        .toBodilessEntity()
                )
                .map(this::extractUserIdFromLocationHeader);
    }

    public Mono<Void> active(String id, Boolean isActive) {
        return authenticateClientCredentials()
                .flatMap(authentication -> keycloakWebClient.put()
                        .uri(uriBuilder -> uriBuilder
                                .path("/admin/realms/{realm}/users/{id}")
                                .build(
                                        environment.getRequiredProperty(REALM),
                                        id
                                )

                        )
                        .header("Authorization", "Bearer " + authentication.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"enabled\": " + isActive + "  }")
                        .retrieve()
                        .toBodilessEntity()
                ).then();

    }


    private String extractUserIdFromLocationHeader(ResponseEntity<Void> responseEntity) {
        return Optional.ofNullable(responseEntity.getHeaders().getLocation())
                .map(URI::getPath)
                .map(location -> location.split("/users/"))
                .filter(locationSplitted -> locationSplitted.length == 2)
                .map(locationSplitted -> locationSplitted[1])
                .orElse(null);
    }

    public Mono<Void> updateUser(String userId, KeycloakUserRequest keycloakUserRequest) {
        return authenticateClientCredentials()
                .flatMap(authentication -> keycloakWebClient.put()
                        .uri(uriBuilder -> uriBuilder
                                .path("/admin/realms/{realm}/users/{userId}")
                                .build(environment.getRequiredProperty(REALM), userId))
                        .header("Authorization", "Bearer " + authentication.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(keycloakUserRequest)
                        .retrieve()
                        .toBodilessEntity()
                )
                .then();
    }
}
