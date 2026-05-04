package com.agendaService.contract.v1;

import com.agendaService.contract.v1.response.AppointmentResponse;
import com.agendaService.contract.v1.response.ReportSummaryResponse;
import com.agendaService.domain.report.port.api.ReportApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/agenda/reports")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Relatórios", description = "Relatórios de atendimentos realizados e pendentes")
public class ReportController {

    private final ReportApiPort reportApiPort;

    @GetMapping("/completed")
    @Operation(summary = "Relatório de atendimentos realizados")
    public Flux<AppointmentResponse> getCompleted(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return reportApiPort.getCompletedAppointments(startDate, endDate)
                .map(AppointmentResponse::fromDomain);
    }

    @GetMapping("/pending")
    @Operation(summary = "Relatório de atendimentos pendentes")
    public Flux<AppointmentResponse> getPending(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return reportApiPort.getPendingAppointments(startDate, endDate)
                .map(AppointmentResponse::fromDomain);
    }

    @GetMapping("/summary")
    @Operation(summary = "Resumo geral de agendamentos no período")
    public Mono<ReportSummaryResponse> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return reportApiPort.getSummary(startDate, endDate)
                .map(ReportSummaryResponse::fromDomain);
    }
}

