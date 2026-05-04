package com.agendaService.domain.appointment.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentInput(
        String userId,
        String serviceId,
        LocalDateTime scheduledAt,
        String notes
) {}

