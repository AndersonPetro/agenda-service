package com.agendaService.infrastructure.adapters;

import com.agendaService.domain.input.SaveUserInput;
import com.agendaService.domain.user.port.spi.OauthServerSpiPort;
import com.agendaService.infrastructure.rest.keycloak.KeycloakIntegration;
import com.agendaService.infrastructure.rest.keycloak.authenticate.request.CredentialRequest;
import com.agendaService.infrastructure.rest.keycloak.authenticate.request.KeycloakUserRequest;
import com.agendaService.infrastructure.rest.keycloak.authenticate.response.KeycloakUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Component
@AllArgsConstructor
public class OauthServerSpiImpl implements OauthServerSpiPort {
    private KeycloakIntegration keycloakIntegration;

    @Override
    public Mono<String> findUserWithCriteria(String username, String email) {
        return Mono.fromCallable(() -> keycloakIntegration.findUserWithCriteria(username, email))
                .subscribeOn(Schedulers.boundedElastic())
                .map(KeycloakUserResponse::getId);
    }

    @Override
    public Mono<Void> updateUser(String userId, SaveUserInput saveUserInput) {
        return Mono.fromRunnable(() -> keycloakIntegration.updateUser(
                userId,
                KeycloakUserRequest.builder()
                        .credentials(buildCredentials(saveUserInput))
                        .username(saveUserInput.getEmail())
                        .email(saveUserInput.getEmail())
                        .build()
        )).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private static List<CredentialRequest> buildCredentials(SaveUserInput saveUserInput) {
        if (StringUtils.hasText((saveUserInput.getPassword())))
            return List.of(CredentialRequest.builder().value(saveUserInput.getPassword()).build());
        return List.of(CredentialRequest.builder().value("#@pv2023").build());
    }
}
