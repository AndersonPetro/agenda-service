package com.agenda.domain.user;

import com.agenda.domain.input.SaveUserInput;
import com.agenda.domain.user.api.UserApiPort;
import com.agenda.domain.user.dtos.UserDto;
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
        return  oauthServerSpiPort.findUserWithCriteria(saveUserInput.getFullName(), saveUserInput.getEmail())
                .flatMap(userId -> updateUser(userId, saveUserInput))
                .switchIfEmpty(Mono.defer(() -> createUser(saveUserInput)));
    }
}
