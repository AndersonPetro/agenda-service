package com.agenda.infrastructure.adapters;

import com.agenda.domain.user.spi.OauthServerSpiPort;
import com.agenda.infrastructure.rest.keycloak.KeycloakIntegration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class OauthServerSpiImpl implements OauthServerSpiPort {
    private KeycloakIntegration keycloakIntegration;
    @Override
    public Mono<String> findUserWithCriteria(String username, String email) {
        return ;
    }
}
