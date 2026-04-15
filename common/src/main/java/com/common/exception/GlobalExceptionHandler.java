package com.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<AgendaHttpExceptionModel>> handleGenericException(Exception ex) {
        log.error("Erro inesperado: ", ex);
        var model = AgendaHttpExceptionModel.builder()
                .code("INTERNAL_ERROR")
                .message("Ocorreu um erro interno no servidor")
                .build();

        return Mono.just(ResponseEntity
                .internalServerError()
                .body(model));
    }
}

