package com.agenda.infrastructure.adapters;

import com.agenda.domain.user.dtos.UserDto;
import com.agenda.domain.user.port.spi.UserSpiPort;
import com.agenda.infrastructure.repositories.covenant.persistent.CovenantRepository;
import com.agenda.infrastructure.repositories.user.mapper.UserMapper;
import com.agenda.infrastructure.repositories.user.persistent.UserEntity;
import com.agenda.infrastructure.repositories.user.persistent.UserRepository;
import com.agenda.infrastructure.repositories.user.persistent.UserRepositoryHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

}
