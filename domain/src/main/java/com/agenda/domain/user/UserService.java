package com.agenda.domain.user;

import com.agenda.domain.covenant.model.CovenantDto;
import com.agenda.domain.covenant.port.api.CovenantApiPort;
import com.agenda.domain.input.SaveUserInput;
import com.agenda.domain.user.port.api.UserApiPort;
import com.agenda.domain.user.dtos.UserDto;
import com.agenda.domain.user.port.spi.OauthServerSpiPort;
import com.agenda.domain.user.port.spi.UserSpiPort;
import com.common.exception.AgendaHttpException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserApiPort {

    private final UserSpiPort userSpiPort;
    private final OauthServerSpiPort oauthServerSpiPort;
    private final CovenantApiPort covenantApiPort;

    @Override
    public Mono<UserDto> save(SaveUserInput saveUserInput) {
        return oauthServerSpiPort.findUserWithCriteria(saveUserInput.getFullName(), saveUserInput.getEmail())
                .flatMap(userId -> updateUser(userId, saveUserInput))
                .switchIfEmpty(Mono.defer(() -> createUser(saveUserInput)));
    }

    private Mono<UserDto> updateUser(String userId, SaveUserInput saveUserInput) {
       return findCovenant(saveUserInput.getCovenants())
               .onErrorMap(AgendaHttpException.class, e ->
                       AgendaHttpException.withHttpStatus(e.getHttpStatus()))
    }

    private Mono<List<CovenantDto>> findCovenant(List<String> covenants) {
        return covenantApiPort.findByCodeIn(covenants)
                .collectList();

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
