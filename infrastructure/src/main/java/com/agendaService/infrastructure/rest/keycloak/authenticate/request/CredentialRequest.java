package com.agendaService.infrastructure.rest.keycloak.authenticate.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialRequest {

    @Builder.Default
    private Boolean temporary = false;
    @Builder.Default
    private String type = "password";
    private String value;

}