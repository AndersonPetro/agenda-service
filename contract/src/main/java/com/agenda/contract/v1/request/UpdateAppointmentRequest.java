package com.agenda.contract.v1.request;

import com.agenda.domain.appointment.model.UpdateAppointmentInput;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {

    @Future(message = "Data do agendamento deve ser no futuro")
    private LocalDateTime scheduledAt;

    private String notes;

    public UpdateAppointmentInput toInput() {
        return UpdateAppointmentInput.builder()
                .scheduledAt(scheduledAt)
                .notes(notes)
                .build();
    }
}

