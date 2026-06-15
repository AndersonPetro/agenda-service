package com.agendaService.infrastructure.repositories.user.mapper;

import com.agendaService.domain.user.dtos.Covenant;
import com.agendaService.domain.user.dtos.UserDto;
import com.agendaService.infrastructure.repositories.user.persistent.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserEntity mapToUserEntity(UserDto user) {
        if (user == null) return null;
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .isActive(user.getIsActive() != null ? user.getIsActive() : true)
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt() : java.time.Instant.now())
                .covenants(user.getCovenants() != null ? user.getCovenants().stream()
                        .map(covenant ->
                                Covenant.builder()
                                        .id(covenant.id())
                                        .code(covenant.code())
                                        .name(covenant.name())
                                        .build()
                        )
                        .toList() : Collections.emptyList()
                )
                .build();
    }

}