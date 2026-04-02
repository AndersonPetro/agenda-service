package com.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class AgendaHttpException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final AgendaHttpExceptionModel agendaHttpExceptionModel;

    public AgendaHttpException(@NonNull HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.agendaHttpExceptionModel = AgendaHttpExceptionModel.builder()
                .message(message)
                .build();
    }

    public AgendaHttpException(@NonNull HttpStatus httpStatus, String code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.agendaHttpExceptionModel = AgendaHttpExceptionModel.builder()
                .code(code)
                .message(message)
                .build();
    }

    public AgendaHttpException(@NonNull HttpStatus httpStatus,
                               String code,
                               String message,
                               List<AgendaFormErrorModel> formErrors) {
        super(message);
        this.httpStatus = httpStatus;
        this.agendaHttpExceptionModel = AgendaHttpExceptionModel.builder()
                .code(code)
                .message(message)
                .formErrors(formErrors)
                .build();
    }

    public AgendaHttpException(@NonNull HttpStatus httpStatus,
                               String code,
                               String message,
                               Map<String, String> details) {
        super(message);
        this.httpStatus = httpStatus;
        this.agendaHttpExceptionModel = AgendaHttpExceptionModel.builder()
                .code(code)
                .message(message)
                .details(details)
                .build();
    }

    public AgendaHttpException(@NonNull HttpStatus httpStatus,
                               String code,
                               String message,
                               List<AgendaFormErrorModel> formErrors,
                               Map<String, String> details) {
        super(message);
        this.httpStatus = httpStatus;
        this.agendaHttpExceptionModel = AgendaHttpExceptionModel.builder()
                .code(code)
                .message(message)
                .formErrors(formErrors)
                .details(details)
                .build();
    }

    public AgendaHttpException(@NonNull HttpStatus httpStatus, @NonNull AgendaHttpExceptionModel agendaHttpExceptionModel) {
        super(agendaHttpExceptionModel.getMessage());
        this.httpStatus = httpStatus;
        this.agendaHttpExceptionModel = agendaHttpExceptionModel;
    }

    // ---- Factory methods ----

    public static AgendaHttpExceptionBuilder withHttpStatus(HttpStatus httpStatus) {
        return new AgendaHttpExceptionBuilder(httpStatus);
    }

    public static AgendaHttpExceptionBuilder withHttp400() {
        return new AgendaHttpExceptionBuilder(HttpStatus.BAD_REQUEST);
    }

    public static AgendaHttpExceptionBuilder withHttp401() {
        return new AgendaHttpExceptionBuilder(HttpStatus.UNAUTHORIZED);
    }

    public static AgendaHttpExceptionBuilder withHttp403() {
        return new AgendaHttpExceptionBuilder(HttpStatus.FORBIDDEN);
    }

    public static AgendaHttpExceptionBuilder withHttp404() {
        return new AgendaHttpExceptionBuilder(HttpStatus.NOT_FOUND);
    }

    public static AgendaHttpExceptionBuilder withHttp408() {
        return new AgendaHttpExceptionBuilder(HttpStatus.REQUEST_TIMEOUT);
    }

    public static AgendaHttpExceptionBuilder withHttp412() {
        return new AgendaHttpExceptionBuilder(HttpStatus.PRECONDITION_FAILED);
    }

    public static AgendaHttpExceptionBuilder withHttp417() {
        return new AgendaHttpExceptionBuilder(HttpStatus.EXPECTATION_FAILED);
    }

    public static AgendaHttpExceptionBuilder withHttp428() {
        return new AgendaHttpExceptionBuilder(HttpStatus.PRECONDITION_REQUIRED);
    }

    public static AgendaHttpExceptionBuilder withHttp500() {
        return new AgendaHttpExceptionBuilder(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static AgendaHttpExceptionBuilder withHttp502() {
        return new AgendaHttpExceptionBuilder(HttpStatus.BAD_GATEWAY);
    }

    public static AgendaHttpExceptionBuilder withHttp503() {
        return new AgendaHttpExceptionBuilder(HttpStatus.SERVICE_UNAVAILABLE);
    }

    // ---- Builder ----

    public static class AgendaHttpExceptionBuilder {

        private final HttpStatus httpStatus;
        private final AgendaHttpExceptionModel agendaHttpExceptionModel;

        public AgendaHttpExceptionBuilder(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            this.agendaHttpExceptionModel = new AgendaHttpExceptionModel();
            this.agendaHttpExceptionModel.setFormErrors(new ArrayList<>());
            this.agendaHttpExceptionModel.setDetails(new HashMap<>());
        }

        public AgendaHttpExceptionBuilder withCode(String code) {
            this.agendaHttpExceptionModel.setCode(code);
            return this;
        }

        public AgendaHttpExceptionBuilder withMessage(String message) {
            this.agendaHttpExceptionModel.setMessage(message);
            return this;
        }

        public AgendaHttpExceptionBuilder withFormError(AgendaFormErrorModel formError) {
            this.agendaHttpExceptionModel.getFormErrors().add(formError);
            return this;
        }

        public AgendaHttpExceptionBuilder withFormError(String field, String message) {
            this.agendaHttpExceptionModel.getFormErrors().add(
                    AgendaFormErrorModel.builder()
                            .field(field)
                            .message(message)
                            .build()
            );
            return this;
        }

        public AgendaHttpExceptionBuilder withFormErrors(List<AgendaFormErrorModel> formErrors) {
            if (formErrors == null || formErrors.isEmpty()) return this;
            this.agendaHttpExceptionModel.setFormErrors(formErrors);
            return this;
        }

        public AgendaHttpExceptionBuilder withDetail(String key, String value) {
            this.agendaHttpExceptionModel.getDetails().put(key, value);
            return this;
        }

        public AgendaHttpExceptionBuilder withDetails(Map<String, String> details) {
            this.agendaHttpExceptionModel.setDetails(details);
            return this;
        }

        public AgendaHttpException build() {
            if (this.agendaHttpExceptionModel.getFormErrors().isEmpty()) {
                this.agendaHttpExceptionModel.setFormErrors(null);
            }
            if (this.agendaHttpExceptionModel.getDetails().isEmpty()) {
                this.agendaHttpExceptionModel.setDetails(null);
            }
            return new AgendaHttpException(this.httpStatus, this.agendaHttpExceptionModel);
        }
    }
}

