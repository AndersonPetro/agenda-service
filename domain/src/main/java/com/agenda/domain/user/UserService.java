package com.agenda.domain.user;

import com.agenda.domain.input.SaveUserInput;
import com.agenda.domain.user.api.UserApiPort;
import com.agenda.domain.user.dtos.UserDto;
import com.agenda.domain.user.spi.OauthServerSpiPort;
import com.agenda.domain.user.spi.UserSpiPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService implements UserApiPort {

    private final UserSpiPort userSpiPort;
    private final OauthServerSpiPort oauthServerSpiPort;

    @Override
    public Mono<UserDto> save(SaveUserInput saveUserInput) {
        return oauthServerSpiPort.findUserWithCriteria(saveUserInput.getFullName(), saveUserInput.getEmail())
                .flatMap(userId -> updateUser(userId, saveUserInput))
                .switchIfEmpty(Mono.defer(() -> createUser(saveUserInput)));
    }

    private Mono<UserDto> updateUser(String userId, SaveUserInput saveUserInput) {
        var user = UserDto.builder()
                .id(userId)
                .email(saveUserInput.getEmail())
                .name(saveUserInput.getFullName())
                .isActive(true)
                .build();
        return userSpiPort.save(user);
    }

    private Mono<UserDto> createUser(SaveUserInput saveUserInput) {
        var user = UserDto.builder()
                .email(saveUserInput.getEmail())
                .name(saveUserInput.getFullName())
                .isActive(true)
                .build();
        return userSpiPort.save(user);
    }
}
