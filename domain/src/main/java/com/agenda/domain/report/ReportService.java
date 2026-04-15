package com.agenda.domain.report;

import com.agenda.domain.appointment.dtos.AppointmentDto;
import com.agenda.domain.appointment.port.spi.AppointmentSpiPort;
import com.agenda.domain.report.dtos.ReportSummaryDto;
import com.agenda.domain.report.port.api.ReportApiPort;
import com.common.enums.AppointmentStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReportService implements ReportApiPort {

    private final AppointmentSpiPort appointmentSpiPort;

    @Override
    public Flux<AppointmentDto> getCompletedAppointments(LocalDate startDate, LocalDate endDate) {
        return appointmentSpiPort.findByStatusAndScheduledAtBetween(
                AppointmentStatusEnum.COMPLETED,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
    }

    @Override
    public Flux<AppointmentDto> getPendingAppointments(LocalDate startDate, LocalDate endDate) {
        return appointmentSpiPort.findByStatusAndScheduledAtBetween(
                AppointmentStatusEnum.PENDING,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
    }

    @Override
    public Mono<ReportSummaryDto> getSummary(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        return appointmentSpiPort.findByScheduledAtBetween(start, end)
                .collectList()
                .map(appointments -> {
                    long completed = appointments.stream()
                            .filter(a -> a.getStatus() == AppointmentStatusEnum.COMPLETED).count();
                    long pending = appointments.stream()
                            .filter(a -> a.getStatus() == AppointmentStatusEnum.PENDING).count();
                    long confirmed = appointments.stream()
                            .filter(a -> a.getStatus() == AppointmentStatusEnum.CONFIRMED).count();
                    long cancelled = appointments.stream()
                            .filter(a -> a.getStatus() == AppointmentStatusEnum.CANCELLED).count();

                    return ReportSummaryDto.builder()
                            .totalAppointments((long) appointments.size())
                            .completedAppointments(completed)
                            .pendingAppointments(pending)
                            .confirmedAppointments(confirmed)
                            .cancelledAppointments(cancelled)
                            .build();
                });
    }
}

