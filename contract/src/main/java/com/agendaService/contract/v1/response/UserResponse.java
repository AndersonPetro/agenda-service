package com.agendaService.contract.v1.response;

import com.agendaService.domain.user.dtos.Covenant;
import com.agendaService.domain.user.dtos.UserDto;
import com.common.enums.RoleEnum;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record UserResponse(
        String id,
        String name,
        String email,
        String phone,
        RoleEnum role,
        Boolean isActive,
        Instant createdAt,
        List<CovenantResponse> covenants
) {

    @Builder
    public record CovenantResponse(
            String id,
            Long code,
            String name
    ) {
    }
    public static UserResponse fromDomain(UserDto user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .covenants(user.getCovenants() != null ? user.getCovenants().stream().map(UserResponse::fromDomain).toList() : List.of())
                .build();
    }

    private static CovenantResponse fromDomain(Covenant covenant){
        return CovenantResponse.builder()
                .id(covenant.id())
                .code(covenant.code())
                .name(covenant.name())
                .build();

    }
}

