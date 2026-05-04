package com.agendaService.contract.v1.response;

import com.agendaService.domain.report.dtos.ReportSummaryDto;
import lombok.Builder;

@Builder
public record ReportSummaryResponse(
        Long totalAppointments,
        Long completedAppointments,
        Long pendingAppointments,
        Long confirmedAppointments,
        Long cancelledAppointments
) {
    public static ReportSummaryResponse fromDomain(ReportSummaryDto dto) {
        return ReportSummaryResponse.builder()
                .totalAppointments(dto.getTotalAppointments())
                .completedAppointments(dto.getCompletedAppointments())
                .pendingAppointments(dto.getPendingAppointments())
                .confirmedAppointments(dto.getConfirmedAppointments())
                .cancelledAppointments(dto.getCancelledAppointments())
                .build();
    }
}

