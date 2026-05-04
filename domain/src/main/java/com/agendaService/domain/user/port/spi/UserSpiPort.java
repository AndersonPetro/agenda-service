package com.agendaService.domain.user.port.spi;

import com.agendaService.domain.user.dtos.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserSpiPort {
    Mono<UserDto> findById(String id);
    Mono<UserDto> save(UserDto user);
    Mono<UserDto> setUserActive(String userId, Boolean isActive);
    Mono<Page<UserDto>> findByFilters(PageRequest pageRequest, Map<String, String> filters);


}
