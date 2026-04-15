package com.agenda.domain.report.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportSummaryDto {
    private Long totalAppointments;
    private Long completedAppointments;
    private Long pendingAppointments;
    private Long confirmedAppointments;
    private Long cancelledAppointments;
}

