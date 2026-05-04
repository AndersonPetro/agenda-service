package com.agendaService.domain.user.port.api;

import com.agendaService.domain.user.dtos.UserDto;
import com.agendaService.domain.input.SaveUserInput;
import reactor.core.publisher.Mono;

public interface UserApiPort {

    Mono<UserDto> save(SaveUserInput saveUserInput);
    Mono<UserDto> findById(String id);


}
