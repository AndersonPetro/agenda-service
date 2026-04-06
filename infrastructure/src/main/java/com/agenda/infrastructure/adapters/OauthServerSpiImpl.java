package com.agenda.infrastructure.adapters;

import com.agenda.domain.user.port.spi.OauthServerSpiPort;
import com.agenda.infrastructure.rest.keycloak.KeycloakIntegration;
import com.agenda.infrastructure.rest.keycloak.authenticate.response.KeycloakUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class OauthServerSpiImpl implements OauthServerSpiPort {
    private KeycloakIntegration keycloakIntegration;
    @Override
    public Mono<String> findUserWithCriteria(String username, String email) {
        return keycloakIntegration.findUserWithCriteria(username,email)
                .map(KeycloakUserResponse::getId);
    }
}
