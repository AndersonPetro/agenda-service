package com.agendaService.domain.auth.port.spi;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthSpiPort {
    Mono<Map<String, Object>> authenticateUser(String email, String password);
    Mono<String> createUser(String firstName, String lastName, String email, String password);
    Mono<String> findUserIdByEmail(String email);
    Mono<Void> resetPassword(String userId, String newPassword);
    Mono<Void> deleteUser(String userId);
}

