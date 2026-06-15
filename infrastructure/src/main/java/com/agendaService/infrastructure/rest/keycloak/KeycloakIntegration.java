package com.agendaService.infrastructure.rest.keycloak;

import com.agendaService.infrastructure.rest.keycloak.authenticate.request.KeycloakUserRequest;
import com.agendaService.infrastructure.rest.keycloak.authenticate.response.KeycloakAuthenticationResponse;
import com.agendaService.infrastructure.rest.keycloak.authenticate.response.KeycloakUserResponse;
import com.common.exception.AgendaHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class KeycloakIntegration {

    private final RestClient keycloakRestClient;
    private final Environment environment;

    private static final String REALM = "restclient.keycloak.realm";

    public KeycloakIntegration(RestClient keycloakRestClient, Environment environment) {
        this.keycloakRestClient = keycloakRestClient;
        this.environment = environment;
    }

    public KeycloakAuthenticationResponse authenticateClientCredentials() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", "app_agenda");
        formData.add("client_secret", "rDDID39IuFVMbdHhjvmr8B0lwqlN5WVf");

        return keycloakRestClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", environment.getRequiredProperty(REALM))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(KeycloakAuthenticationResponse.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> authenticateUser(String email, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", "app_agenda");
        formData.add("client_secret", "rDDID39IuFVMbdHhjvmr8B0lwqlN5WVf");
        formData.add("username", email);
        formData.add("password", password);

        return keycloakRestClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", environment.getRequiredProperty(REALM))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(Map.class);
    }

    public KeycloakUserResponse findUserWithCriteria(String username, String email) {
        KeycloakAuthenticationResponse authentication = authenticateClientCredentials();

        KeycloakUserResponse[] response = keycloakRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/realms/{realm}/users")
                        .queryParamIfPresent("username", Optional.ofNullable(username))
                        .queryParamIfPresent("email", Optional.ofNullable(email))
                        .build(environment.getRequiredProperty(REALM)))
                .header("Authorization", "Bearer " + authentication.getAccessToken())
                .retrieve()
                .body(KeycloakUserResponse[].class);

        List<KeycloakUserResponse> users = Arrays.stream(
                        Optional.ofNullable(response).orElse(new KeycloakUserResponse[0]))
                .filter(user -> (username != null && username.equals(user.getUsername()))
                        || (email != null && email.equals(user.getEmail())))
                .toList();

        if (users.size() == 1) {
            return users.get(0);
        }
        if (users.isEmpty()) {
            log.info("Nenhum usuário encontrado para os filtros username={}, email={}", username, email);
            return null;
        }
        log.warn("Mais de um usuário retornado para os filtros username={}, email={}. Cancelando operação.", username, email);
        throw AgendaHttpException.withHttp412()
                .withMessage("Não é possível identificar o usuário")
                .build();
    }

    public List<KeycloakUserResponse> findAllUsers() {
        KeycloakAuthenticationResponse authentication = authenticateClientCredentials();

        KeycloakUserResponse[] response = keycloakRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/realms/{realm}/users")
                        .queryParam("max", 1000)
                        .build(environment.getRequiredProperty(REALM)))
                .header("Authorization", "Bearer " + authentication.getAccessToken())
                .retrieve()
                .body(KeycloakUserResponse[].class);

        return response != null ? Arrays.asList(response) : List.of();
    }

    public String createUser(KeycloakUserRequest keycloakUserRequest) {
        KeycloakAuthenticationResponse authentication = authenticateClientCredentials();

        ResponseEntity<Void> responseEntity = keycloakRestClient.post()
                .uri("/admin/realms/{realm}/users", environment.getRequiredProperty(REALM))
                .header("Authorization", "Bearer " + authentication.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(keycloakUserRequest)
                .retrieve()
                .toBodilessEntity();

        return extractUserIdFromLocationHeader(responseEntity);
    }

    public void active(String id, Boolean isActive) {
        KeycloakAuthenticationResponse authentication = authenticateClientCredentials();

        keycloakRestClient.put()
                .uri("/admin/realms/{realm}/users/{id}", environment.getRequiredProperty(REALM), id)
                .header("Authorization", "Bearer " + authentication.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("enabled", isActive))
                .retrieve()
                .toBodilessEntity();
    }

    public void updateUser(String userId, KeycloakUserRequest keycloakUserRequest) {
        KeycloakAuthenticationResponse authentication = authenticateClientCredentials();

        keycloakRestClient.put()
                .uri("/admin/realms/{realm}/users/{userId}", environment.getRequiredProperty(REALM), userId)
                .header("Authorization", "Bearer " + authentication.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(keycloakUserRequest)
                .retrieve()
                .toBodilessEntity();
    }

    public void resetPassword(String userId, String newPassword) {
        KeycloakAuthenticationResponse authentication = authenticateClientCredentials();

        var credential = Map.of(
                "type", "password",
                "value", newPassword,
                "temporary", false
        );

        keycloakRestClient.put()
                .uri("/admin/realms/{realm}/users/{userId}/reset-password", environment.getRequiredProperty(REALM), userId)
                .header("Authorization", "Bearer " + authentication.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(credential)
                .retrieve()
                .toBodilessEntity();
    }

    private String extractUserIdFromLocationHeader(ResponseEntity<Void> responseEntity) {
        return Optional.ofNullable(responseEntity.getHeaders().getLocation())
                .map(URI::getPath)
                .map(location -> location.split("/users/"))
                .filter(locationSplitted -> locationSplitted.length == 2)
                .map(locationSplitted -> locationSplitted[1])
                .orElse(null);
    }
}