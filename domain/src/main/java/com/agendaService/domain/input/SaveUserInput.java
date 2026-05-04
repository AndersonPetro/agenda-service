package com.agendaService.domain.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserInput {

    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<Long> covenants;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}