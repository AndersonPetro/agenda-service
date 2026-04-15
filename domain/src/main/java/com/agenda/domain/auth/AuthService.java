package com.agenda.domain.auth;

import com.agenda.domain.auth.port.api.AuthApiPort;
import com.agenda.domain.auth.port.spi.AuthSpiPort;
import com.agenda.domain.user.dtos.UserDto;
import com.agenda.domain.user.port.spi.UserSpiPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService implements AuthApiPort {

    private final AuthSpiPort authSpiPort;
    private final UserSpiPort userSpiPort;

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
                            .thenReturn(keycloakUserId);
                });
    }
}

