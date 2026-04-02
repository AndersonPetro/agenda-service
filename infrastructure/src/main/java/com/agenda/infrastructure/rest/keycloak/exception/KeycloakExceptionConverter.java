package com.agenda.infrastructure.rest.keycloak.exception;

import com.common.exception.AgendaHttpException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Slf4j
@AllArgsConstructor
public class KeycloakExceptionConverter {

    private final ObjectMapper objectMapper;

    public AgendaHttpException convert(URI url, HttpStatus httpStatus, String responseBody) {
        try {
            KeycloakExceptionModel keycloakExceptionModel = objectMapper.readValue(responseBody, KeycloakExceptionModel.class);
            KeycloakResponseMetadata metadata = convert(keycloakExceptionModel);
            return new AgendaHttpException(metadata.httpStatus(), metadata.code(), metadata.message());
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private KeycloakResponseMetadata convert(KeycloakExceptionModel keycloakException) {
        String defaultMessage = "Ocorreu um erro ao processar a requisição";
        HttpStatus defaultHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if ("User exists with same username".equals(keycloakException.getErrorMessage())) {
            return new KeycloakResponseMetadata(null, "Usuário já cadastrado", HttpStatus.CONFLICT);
        }
        if ("Invalid refresh token".equals(keycloakException.getErrorDescription())
                || "Token is not active".equals(keycloakException.getErrorDescription())) {
            return new KeycloakResponseMetadata("100", "Credenciais inválidas", HttpStatus.UNAUTHORIZED);
        }
        if (keycloakException.getError() == null) {
            return new KeycloakResponseMetadata(null, defaultMessage, defaultHttpStatus);
        }

        return switch (keycloakException.getError()) {
            case "invalid_grant" ->
                    new KeycloakResponseMetadata(null, "Credenciais inválidas", HttpStatus.UNAUTHORIZED);
            case "Realm does not exist" -> {
                log.error("Realm de acesso ao Keycloak inválido: " + keycloakException);
                yield new KeycloakResponseMetadata(null, defaultMessage, defaultHttpStatus);
            }
            case "invalid_client", "unauthorized_client" -> {
                log.error("Credenciais da aplicação de acesso ao Keycloak estão inválidas: " + keycloakException);
                yield new KeycloakResponseMetadata(null, defaultMessage, defaultHttpStatus);
            }
            default -> {
                log.error("Erro não mapeado na resposta do Keycloak: " + keycloakException);
                yield new KeycloakResponseMetadata(null, defaultMessage, defaultHttpStatus);
            }
        };
    }

    private record KeycloakResponseMetadata(String code, String message, HttpStatus httpStatus) {
    }

}