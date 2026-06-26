package com.agendaService.domain.auth;

import com.agendaService.domain.auth.port.api.AuthApiPort;
import com.agendaService.domain.auth.port.spi.AuthSpiPort;
import com.agendaService.domain.user.dtos.UserDto;
import com.agendaService.domain.user.port.spi.UserSpiPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService implements AuthApiPort {

    private final AuthSpiPort authSpiPort;
    private final UserSpiPort userSpiPort;

    private static final Map<String, RecoveryCodeInfo> recoveryCodes = new ConcurrentHashMap<>();

    private record RecoveryCodeInfo(String code, java.time.Instant expiresAt) {
        public boolean isExpired() {
            return java.time.Instant.now().isAfter(expiresAt);
        }
    }

    @Override
    public Mono<Map<String, Object>> login(String email, String password) {
        return authSpiPort.authenticateUser(email, password);
    }

    @Override
    public Mono<String> signup(String firstName, String lastName, String email, String password) {
        return authSpiPort.createUser(firstName, lastName, email, password)
                .flatMap(keycloakUserId -> {
                    var user = UserDto.builder()
                            .id(keycloakUserId)
                            .email(email)
                            .name(firstName + " " + lastName)
                            .isActive(true)
                            .build();
                    return userSpiPort.save(user)
                            .thenReturn(keycloakUserId)
                            .onErrorResume(error -> {
                                log.error("[SIGNUP] Erro ao salvar usuário no MongoDB. Removendo do Keycloak...", error);
                                return authSpiPort.deleteUser(keycloakUserId)
                                        .onErrorResume(deleteError -> {
                                            log.error("[SIGNUP] Erro ao tentar remover usuário do Keycloak após falha no MongoDB", deleteError);
                                            return Mono.empty();
                                        })
                                        .then(Mono.error(error));
                            });
                });
    }

    @Override
    public Mono<Void> requestPasswordRecovery(String email) {
        return authSpiPort.findUserIdByEmail(email)
                .switchIfEmpty(Mono.error(com.common.exception.AgendaHttpException.withHttp404()
                        .withMessage("Usuário não encontrado com o e-mail informado")
                        .build()))
                .flatMap(userId -> {
                    if (userId == null || userId.isBlank()) {
                        return Mono.error(com.common.exception.AgendaHttpException.withHttp404()
                                .withMessage("Usuário não encontrado com o e-mail informado")
                                .build());
                    }

                    // Generate a 6-digit code
                    String code = String.format("%06d", new java.util.Random().nextInt(1000000));
                    log.info("[PASSWORD RECOVERY] Generated code {} for email {}", code, email);

                    recoveryCodes.put(email.toLowerCase(), new RecoveryCodeInfo(code, java.time.Instant.now().plusSeconds(900))); // 15 mins

                    return Mono.empty();
                })
                .then();
    }

    @Override
    public Mono<Void> confirmPasswordRecovery(String email, String code, String newPassword) {
        return Mono.defer(() -> {
            var info = recoveryCodes.get(email.toLowerCase());
            if (info == null || info.isExpired() || !info.code().equals(code)) {
                return Mono.error(com.common.exception.AgendaHttpException.withHttp400()
                        .withMessage("Código de validação inválido ou expirado")
                        .build());
            }

            return authSpiPort.findUserIdByEmail(email)
                    .switchIfEmpty(Mono.error(com.common.exception.AgendaHttpException.withHttp404()
                            .withMessage("Usuário não encontrado")
                            .build()))
                    .flatMap(userId -> {
                        if (userId == null || userId.isBlank()) {
                            return Mono.error(com.common.exception.AgendaHttpException.withHttp404()
                                    .withMessage("Usuário não encontrado")
                                    .build());
                        }

                        return authSpiPort.resetPassword(userId, newPassword)
                                .then(Mono.fromRunnable(() -> recoveryCodes.remove(email.toLowerCase())));
                    });
        }).then();
    }

    @Override
    public String getRecoveryCode(String email) {
        var info = recoveryCodes.get(email.toLowerCase());
        return (info != null && !info.isExpired()) ? info.code() : null;
    }
}

