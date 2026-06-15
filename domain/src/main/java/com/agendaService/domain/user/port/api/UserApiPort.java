package com.agendaService.domain.user.port.api;

import com.agendaService.domain.user.dtos.UserDto;
import com.agendaService.domain.input.SaveUserInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserApiPort {

    Mono<UserDto> save(SaveUserInput saveUserInput);
    Mono<UserDto> findById(String id);
    Mono<Page<UserDto>> findByFilters(PageRequest pageRequest, Map<String, String> filters);
    Mono<UserDto> setUserActive(String id, Boolean isActive);
    Mono<UserDto> update(String id, UserDto userDto);
}

