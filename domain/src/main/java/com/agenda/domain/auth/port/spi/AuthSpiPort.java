package com.agenda.domain.auth.port.spi;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthSpiPort {
    Mono<Map<String, Object>> authenticateUser(String email, String password);
    Mono<String> createUser(String firstName, String lastName, String email, String password);
}

