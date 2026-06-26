package com.agendaService.infrastructure.runner;

import com.agendaService.domain.user.dtos.UserDto;
import com.agendaService.domain.user.port.spi.UserSpiPort;
import com.agendaService.infrastructure.rest.keycloak.KeycloakIntegration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserSyncRunner implements ApplicationRunner {

    private final KeycloakIntegration keycloakIntegration;
    private final UserSpiPort userSpiPort;

    @Override
    public void run(ApplicationArguments args) {
        log.info("[USER SYNC] Iniciando sincronização de usuários do Keycloak para o MongoDB...");

        Flux.defer(() -> {
            try {
                var keycloakUsers = keycloakIntegration.findAllUsers();
                return Flux.fromIterable(keycloakUsers);
            } catch (Exception e) {
                log.error("[USER SYNC] Erro ao carregar usuários do Keycloak", e);
                return Flux.error(e);
            }
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(keycloakUser -> {
            var userId = keycloakUser.getId();
            return userSpiPort.findById(userId)
                    .switchIfEmpty(Mono.defer(() -> {
                        log.info("[USER SYNC] Sincronizando usuário ausente do Keycloak para o MongoDB: {} ({})", keycloakUser.getEmail(), keycloakUser.getId());
                        
                        String name = "";
                        if (keycloakUser.getFirstName() != null) {
                            name += keycloakUser.getFirstName().trim();
                        }
                        if (keycloakUser.getLastName() != null) {
                            if (!name.isEmpty()) name += " ";
                            name += keycloakUser.getLastName().trim();
                        }
                        if (name.isEmpty()) {
                            name = keycloakUser.getUsername();
                        }

                        var userDto = UserDto.builder()
                                .id(userId)
                                .email(keycloakUser.getEmail())
                                .name(name)
                                .isActive(keycloakUser.getEnabled() != null ? keycloakUser.getEnabled() : true)
                                .createdAt(keycloakUser.getCreatedTimestamp() != null ? Instant.ofEpochMilli(keycloakUser.getCreatedTimestamp()) : Instant.now())
                                .build();
                        return userSpiPort.save(userDto);
                    }));
        })
        .subscribe(
                user -> log.info("[USER SYNC] Usuário sincronizado/verificado: {}", user.getEmail()),
                error -> log.error("[USER SYNC] Erro durante a sincronização de usuário", error),
                () -> log.info("[USER SYNC] Sincronização de usuários concluída com sucesso.")
        );
    }
}
