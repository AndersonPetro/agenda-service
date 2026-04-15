package com.agenda.domain.appointment.port.spi;

import com.agenda.domain.appointment.dtos.AppointmentDto;
import com.common.enums.AppointmentStatusEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface AppointmentSpiPort {
    Mono<AppointmentDto> save(AppointmentDto appointmentDto);
    Mono<AppointmentDto> findById(String id);
    Flux<AppointmentDto> findByUserId(String userId);
    Flux<AppointmentDto> findAll();
    Flux<AppointmentDto> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);
    Flux<AppointmentDto> findByStatus(AppointmentStatusEnum status);
    Flux<AppointmentDto> findByStatusAndScheduledAtBetween(AppointmentStatusEnum status, LocalDateTime start, LocalDateTime end);
}

