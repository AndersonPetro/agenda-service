package com.agenda.domain.user.port.spi;

import com.agenda.domain.user.dtos.UserDto;
import reactor.core.publisher.Mono;

public interface UserSpiPort {

    Mono<UserDto> save(UserDto user);

}
