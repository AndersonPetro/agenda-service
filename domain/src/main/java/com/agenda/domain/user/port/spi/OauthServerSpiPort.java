package com.agenda.domain.user.port.spi;

import reactor.core.publisher.Mono;

public interface OauthServerSpiPort {

    Mono<String> findUserWithCriteria(String fullName, String email);

}

