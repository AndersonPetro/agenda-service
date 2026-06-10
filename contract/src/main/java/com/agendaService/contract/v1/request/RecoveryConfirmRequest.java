package com.agendaService.contract.v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryConfirmRequest {

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Deve ser um e-mail válido")
    private String email;

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 6, max = 6, message = "O código deve ter 6 dígitos")
    private String code;

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
    private String newPassword;
}
