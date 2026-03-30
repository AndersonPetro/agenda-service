package com.agenda.contract.v1.response;

import com.common.enums.RoleEnum;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String nome,
        String email,
        String senha,
        RoleEnum role
) {
}
