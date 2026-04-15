package com.agenda.domain.report.port.api;

import com.agenda.domain.appointment.dtos.AppointmentDto;
import com.agenda.domain.report.dtos.ReportSummaryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ReportApiPort {
    Flux<AppointmentDto> getCompletedAppointments(LocalDate startDate, LocalDate endDate);
    Flux<AppointmentDto> getPendingAppointments(LocalDate startDate, LocalDate endDate);
    Mono<ReportSummaryDto> getSummary(LocalDate startDate, LocalDate endDate);
}

