package com.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AgendaHttpExceptionModel {

    private String code;
    private String message;
    private String traceId;
    private List<AgendaFormErrorModel> formErrors;
    private Map<String, String> details;

    public static AgendaHttpExceptionModelBuilder builder() {
        return new AgendaHttpExceptionModelBuilder();
    }

    public static class AgendaHttpExceptionModelBuilder {

        private final AgendaHttpExceptionModel agendaHttpExceptionModel;

        public AgendaHttpExceptionModelBuilder() {
            this.agendaHttpExceptionModel = new AgendaHttpExceptionModel();
            this.agendaHttpExceptionModel.setFormErrors(new ArrayList<>());
            this.agendaHttpExceptionModel.setDetails(new HashMap<>());
        }

        public AgendaHttpExceptionModelBuilder code(String code) {
            this.agendaHttpExceptionModel.setCode(code);
            return this;
        }

        public AgendaHttpExceptionModelBuilder message(String message) {
            this.agendaHttpExceptionModel.setMessage(message);
            return this;
        }

        public AgendaHttpExceptionModelBuilder formErrors(List<AgendaFormErrorModel> formErrors) {
            this.agendaHttpExceptionModel.setFormErrors(formErrors);
            return this;
        }

        public AgendaHttpExceptionModelBuilder formError(String field, String message) {
            if (field == null || message == null) return this;
            this.agendaHttpExceptionModel.getFormErrors().add(
                    AgendaFormErrorModel.builder()
                            .field(field)
                            .message(message)
                            .build()
            );
            return this;
        }

        public AgendaHttpExceptionModelBuilder details(Map<String, String> details) {
            this.agendaHttpExceptionModel.setDetails(details);
            return this;
        }

        public AgendaHttpExceptionModelBuilder detail(String key, String value) {
            if (key == null || value == null) return this;
            this.agendaHttpExceptionModel.getDetails().put(key, value);
            return this;
        }

        public AgendaHttpExceptionModel build() {
            return this.agendaHttpExceptionModel;
        }
    }
}

