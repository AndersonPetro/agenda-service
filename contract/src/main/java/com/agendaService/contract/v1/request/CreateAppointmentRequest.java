package com.agendaService.contract.v1.request;

import com.agendaService.domain.appointment.model.AppointmentInput;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {

    @NotBlank(message = "ID do usuário é obrigatório")
    private String userId;

    @NotBlank(message = "ID do serviço é obrigatório")
    private String serviceId;

    @NotNull(message = "Data/hora do agendamento é obrigatória")
    @Future(message = "Data do agendamento deve ser no futuro")
    private LocalDateTime scheduledAt;

    private String notes;

    public AppointmentInput toInput() {
        return AppointmentInput.builder()
                .userId(userId)
                .serviceId(serviceId)
                .scheduledAt(scheduledAt)
                .notes(notes)
                .build();
    }
}

