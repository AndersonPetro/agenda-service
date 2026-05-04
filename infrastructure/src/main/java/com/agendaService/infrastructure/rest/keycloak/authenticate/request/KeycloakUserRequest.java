package com.agendaService.infrastructure.rest.keycloak.authenticate.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserRequest {

    private String username;
    private List<CredentialRequest> credentials;
    private String firstName;
    private String lastName;
    private String email;
    private Attributes attributes;

    @Builder.Default
    private Boolean emailVerified = true;
    @Builder.Default
    private Boolean enabled = true;

    @Builder
    public record Attributes(
            Long covenantCode
    ) {
    }

}
