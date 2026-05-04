package com.agendaService.domain.appointment.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateAppointmentInput(
        LocalDateTime scheduledAt,
        String notes
) {}

