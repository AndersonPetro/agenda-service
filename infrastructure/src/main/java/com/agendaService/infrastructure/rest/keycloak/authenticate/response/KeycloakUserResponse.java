package com.agendaService.infrastructure.rest.keycloak.authenticate.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserResponse {

    private String id;
    private String username;
    private Boolean enabled;
    private Long createdTimestamp;
    private String email;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;
    private Boolean totp;
}
