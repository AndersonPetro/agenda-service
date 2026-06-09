package com.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AgendaHttpException.class)
    public Mono<ResponseEntity<AgendaHttpExceptionModel>> handleAgendaHttpException(AgendaHttpException ex) {
        log.warn("AgendaHttpException: status={}, message={}", ex.getHttpStatus(), ex.getMessage());
        return Mono.just(ResponseEntity
                .status(ex.getHttpStatus())
                .body(ex.getAgendaHttpExceptionModel()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<AgendaHttpExceptionModel>> handleValidationException(WebExchangeBindException ex) {
        var formErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> AgendaFormErrorModel.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        var model = AgendaHttpExceptionModel.builder()
                .code("VALIDATION_ERROR")
                .message("Erro de validação nos campos enviados")
                .formErrors(formErrors)
                .build();

        return Mono.just(ResponseEntity
                .badRequest()
                .body(model));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public Mono<ResponseEntity<AgendaHttpExceptionModel>> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.warn("HttpClientErrorException: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
        
        if (ex.getStatusCode() == HttpStatus.CONFLICT) {
            var model = AgendaHttpExceptionModel.builder()
                    .code("USER_ALREADY_EXISTS")
                    .message("Este e-mail já está cadastrado no sistema")
                    .build();
            return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(model));
        }
        
        var model = AgendaHttpExceptionModel.builder()
                .code("EXTERNAL_SERVICE_ERROR")
                .message("Erro na comunicação com o serviço de autenticação")
                .build();
        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(model));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<AgendaHttpExceptionModel>> handleGenericException(Exception ex) {

        if (isAccessDeniedException(ex)) {
            return Mono.error(ex);
        }
        log.error("Erro inesperado: ", ex);
        var model = AgendaHttpExceptionModel.builder()
                .code("INTERNAL_ERROR")
                .message("Ocorreu um erro interno no servidor")
                .build();

        return Mono.just(ResponseEntity
                .internalServerError()
                .body(model));
    }

    private boolean isAccessDeniedException(Throwable ex) {
        while (ex != null) {
            if (ex.getClass().getName().contains("AccessDeniedException")
                    || ex.getClass().getName().contains("AuthorizationDeniedException")) {
                return true;
            }
            ex = ex.getCause();
        }
        return false;
    }
}

