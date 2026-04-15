package com.agenda.infrastructure.repositories.appointment.persistent;

import com.agenda.domain.appointment.dtos.AppointmentDto;
import com.common.enums.AppointmentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appointments")
public class AppointmentEntity {

    @Id
    private String id;
    @Indexed
    private String userId;
    @Indexed
    private String serviceId;
    private String serviceName;
    private String userName;
    @Indexed
    private LocalDateTime scheduledAt;
    @Indexed
    private AppointmentStatusEnum status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AppointmentDto toDomain() {
        return AppointmentDto.builder()
                .id(id)
                .userId(userId)
                .serviceId(serviceId)
                .serviceName(serviceName)
                .userName(userName)
                .scheduledAt(scheduledAt)
                .status(status)
                .notes(notes)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static AppointmentEntity fromDomain(AppointmentDto dto) {
        return AppointmentEntity.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .serviceId(dto.getServiceId())
                .serviceName(dto.getServiceName())
                .userName(dto.getUserName())
                .scheduledAt(dto.getScheduledAt())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

