package com.agenda.domain.user.port.spi;

import com.agenda.domain.input.SaveUserInput;
import reactor.core.publisher.Mono;

public interface OauthServerSpiPort {

    Mono<String> findUserWithCriteria(String fullName, String email);
    Mono<Void> updateUser(String userId, SaveUserInput saveUserInput);

}

