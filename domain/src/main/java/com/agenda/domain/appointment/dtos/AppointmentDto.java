package com.agenda.domain.appointment.dtos;

import com.common.enums.AppointmentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private String id;
    private String userId;
    private String serviceId;
    private String serviceName;
    private String userName;
    private LocalDateTime scheduledAt;
    private AppointmentStatusEnum status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

