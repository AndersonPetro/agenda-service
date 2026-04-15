package com.agenda.contract.v1.request;

import com.agenda.domain.service.model.ServiceInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveServiceRequest {

    @NotBlank(message = "Nome do serviço é obrigatório")
    private String name;

    @NotBlank(message = "Descrição do serviço é obrigatória")
    private String description;

    @NotNull(message = "Valor do serviço é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal price;

    @NotNull(message = "Duração do serviço é obrigatória")
    @Positive(message = "Duração deve ser maior que zero")
    private Integer durationMinutes;

    public ServiceInput toInput() {
        return ServiceInput.builder()
                .name(name)
                .description(description)
                .price(price)
                .durationMinutes(durationMinutes)
                .build();
    }
}

