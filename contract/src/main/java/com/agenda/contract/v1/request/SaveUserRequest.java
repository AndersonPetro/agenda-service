package com.agenda.contract.v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import com.agenda.domain.input.SaveUserInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserRequest {

    private String password;
    @NotBlank(message = "Primeiro nome é obrigatório")
    private String firstName;
    @NotBlank(message = "Sobrenome é obrigatório")
    private String lastName;
    @Email(message = "Deve ser um e-mail válido")
    private String email;
    @NotEmpty(message = "O usuário deve estar vinculado a pelo menos 1 convênio")
    private List<Long> covenants;

    public SaveUserInput toInput() {
        return SaveUserInput.builder()
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

}