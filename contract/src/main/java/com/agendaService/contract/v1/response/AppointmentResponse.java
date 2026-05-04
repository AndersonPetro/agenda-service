package com.agendaService.contract.v1.response;

import com.agendaService.domain.appointment.dtos.AppointmentDto;
import com.common.enums.AppointmentStatusEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentResponse(
        String id,
        String userId,
        String serviceId,
        String serviceName,
        String userName,
        LocalDateTime scheduledAt,
        AppointmentStatusEnum status,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AppointmentResponse fromDomain(AppointmentDto dto) {
        return AppointmentResponse.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .serviceId(dto.getServiceId())
                .serviceName(dto.getServiceName())
                .userName(dto.getUserName())
                .scheduledAt(dto.getScheduledAt())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

