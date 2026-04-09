package com.agenda.domain.user.port.api;

import com.agenda.domain.user.dtos.UserDto;
import com.agenda.domain.input.SaveUserInput;
import reactor.core.publisher.Mono;

public interface UserApiPort {

    Mono<UserDto> save(SaveUserInput saveUserInput);
    Mono<UserDto> findById(String id);


}
