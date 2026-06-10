package com.agendaService.contract.v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryRequest {

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Deve ser um e-mail válido")
    private String email;
}
