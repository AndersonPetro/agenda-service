package com.agendaService.domain.appointment.port.api;

import com.agendaService.domain.appointment.dtos.AppointmentDto;
import com.agendaService.domain.appointment.dtos.TimeSlotDto;
import com.agendaService.domain.appointment.model.AppointmentInput;
import com.agendaService.domain.appointment.model.UpdateAppointmentInput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface AppointmentApiPort {
    Mono<AppointmentDto> create(AppointmentInput input);
    Mono<AppointmentDto> findById(String id);
    Flux<AppointmentDto> findByUserId(String userId);
    Flux<AppointmentDto> findAll();
    Mono<AppointmentDto> update(String id, UpdateAppointmentInput input);
    Mono<AppointmentDto> cancel(String id);
    Mono<AppointmentDto> confirm(String id);
    Mono<AppointmentDto> complete(String id);
    Flux<TimeSlotDto> findAvailableSlots(LocalDate date, String serviceId);
}

