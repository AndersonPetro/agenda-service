package com.agenda.contract.v1.response;

import com.agenda.domain.service.dtos.ServiceDto;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ServiceResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        Integer durationMinutes,
        Boolean isActive
) {
    public static ServiceResponse fromDomain(ServiceDto dto) {
        return ServiceResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .isActive(dto.getIsActive())
                .build();
    }
}

