package com.agendaService.domain.user.dtos;

import lombok.Builder;

@Builder
public record Covenant(
        String id,
        Long code,
        String name) {
}
