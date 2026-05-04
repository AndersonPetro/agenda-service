package com.agendaService.infrastructure.rest.keycloak.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakExceptionModel {

    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    private String errorMessage;

}
