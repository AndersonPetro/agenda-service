package com.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {

    public static AgendaHttpException notFoundException(String message) {
        return AgendaHttpException.withHttp404()
                .withMessage(message)
                .build();
    }

    public static AgendaHttpException badRequest(String message) {
        return AgendaHttpException.withHttp400()
                .withMessage(message)
                .build();
    }

    public static AgendaHttpException runTimeException(String message) {
        return AgendaHttpException.withHttp500()
                .withMessage(message)
                .build();
    }

    public static AgendaHttpException conflict(String message) {
        return AgendaHttpException.withHttpStatus(HttpStatus.CONFLICT)
                .withMessage(message)
                .build();
    }
}
