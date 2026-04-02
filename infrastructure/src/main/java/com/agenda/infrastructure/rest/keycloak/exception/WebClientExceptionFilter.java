package com.agenda.infrastructure.rest.keycloak.exception;

import com.common.exception.AgendaHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientExceptionFilter implements ExchangeFilterFunction {

    private final KeycloakExceptionConverter exceptionConverter;

    public WebClientExceptionFilter() {
        this.exceptionConverter = null;
    }

    public WebClientExceptionFilter(KeycloakExceptionConverter exceptionConverter) {
        this.exceptionConverter = exceptionConverter;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .flatMap(response -> {
                    if (response.statusCode().isError()) {
                        HttpStatus httpStatus = HttpStatus.resolve(response.statusCode().value());
                        if (httpStatus == null) {
                            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                        }
                        HttpStatus finalHttpStatus = httpStatus;
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> {
                                    if (exceptionConverter != null) {
                                        AgendaHttpException converted = exceptionConverter.convert(
                                                request.url(), finalHttpStatus, body);
                                        if (converted != null) {
                                            return Mono.error(converted);
                                        }
                                    }
                                    return Mono.error(new AgendaHttpException(
                                            finalHttpStatus,
                                            "Erro na requisição: " + finalHttpStatus.value()));
                                });
                    }
                    return Mono.just(response);
                });
    }
}

