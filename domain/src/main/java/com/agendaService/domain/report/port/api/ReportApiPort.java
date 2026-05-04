package com.agendaService.domain.report.port.api;

import com.agendaService.domain.appointment.dtos.AppointmentDto;
import com.agendaService.domain.report.dtos.ReportSummaryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ReportApiPort {
    Flux<AppointmentDto> getCompletedAppointments(LocalDate startDate, LocalDate endDate);
    Flux<AppointmentDto> getPendingAppointments(LocalDate startDate, LocalDate endDate);
    Mono<ReportSummaryDto> getSummary(LocalDate startDate, LocalDate endDate);
}

