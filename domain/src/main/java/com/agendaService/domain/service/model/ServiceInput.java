package com.agendaService.domain.service.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ServiceInput(
        String name,
        String description,
        BigDecimal price,
        Integer durationMinutes
) {}

