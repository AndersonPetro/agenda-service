package com.agendaService.infrastructure.adapters;

import com.agendaService.domain.user.dtos.UserDto;
import com.agendaService.domain.user.port.spi.UserSpiPort;
import com.agendaService.infrastructure.repositories.user.mapper.UserMapper;
import com.agendaService.infrastructure.repositories.user.persistent.UserEntity;
import com.agendaService.infrastructure.repositories.user.persistent.UserRepository;
import com.agendaService.infrastructure.repositories.user.persistent.UserRepositoryHandler;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@AllArgsConstructor
public class UserSpiImpl implements UserSpiPort {
    private final UserRepository userRepository;
    private final UserRepositoryHandler repository;

    @Override
    public Mono<UserDto> findById(String id) {
        return userRepository.findById(id)
                .mapNotNull(UserEntity::toDomain);
    }

    @Override
    public Mono<UserDto> save(UserDto user) {
        return userRepository.save(UserMapper.mapToUserEntity(user))
                .mapNotNull(UserEntity::toDomain);
    }

    @Override
    public Mono<UserDto> setUserActive(String userId, Boolean isActive) {
        return userRepository.findById(userId)
                .flatMap(entity -> {
                    entity.setIsActive(isActive);
                    return userRepository.save(entity);
                })
                .mapNotNull(UserEntity::toDomain);
    }

    @Override
    public Mono<Page<UserDto>> findByFilters(PageRequest pageRequest, Map<String, String> filters) {
        return repository.findAllWithFilters(pageRequest, filters)
                .map(UserEntity::toDomain)
                .collectList()
                .zipWith(repository.countAllWithFilters(filters))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
    }
}
