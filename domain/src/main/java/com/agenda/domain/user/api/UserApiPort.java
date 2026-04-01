package com.agenda.domain.user.api;

import com.agenda.domain.user.dtos.UserDto;
import com.agenda.domain.input.SaveUserInput;
import reactor.core.publisher.Mono;

public interface UserApiPort {

    Mono<UserDto> save(SaveUserInput saveUserInput);


}
