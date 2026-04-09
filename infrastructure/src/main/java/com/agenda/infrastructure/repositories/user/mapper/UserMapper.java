package com.agenda.infrastructure.repositories.user.mapper;

import com.agenda.domain.user.dtos.Covenant;
import com.agenda.domain.user.dtos.UserDto;
import com.agenda.infrastructure.repositories.user.persistent.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserEntity mapToUserEntity(UserDto user) {
        if (user == null) return null;
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .covenants(user.getCovenants().stream()
                        .map(covenant ->
                                Covenant.builder()
                                        .id(covenant.id())
                                        .code(covenant.code())
                                        .name(covenant.name())
                                        .build()
                        )
                        .toList()
                )
                .build();
    }

}