package com.agendaService.domain.auth.port.api;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthApiPort {
    Mono<Map<String, Object>> login(String email, String password);
    Mono<String> signup(String firstName, String lastName, String email, String password);
    Mono<Void> requestPasswordRecovery(String email);
    Mono<Void> confirmPasswordRecovery(String email, String code, String newPassword);
    default String getRecoveryCode(String email) {
        return null;
    }
}

