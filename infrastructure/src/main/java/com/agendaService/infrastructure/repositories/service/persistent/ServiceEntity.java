package com.agendaService.infrastructure.repositories.service.persistent;

import com.agendaService.domain.service.dtos.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "services")
public class ServiceEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    @Builder.Default
    private Boolean isActive = true;

    public ServiceDto toDomain() {
        return ServiceDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .durationMinutes(durationMinutes)
                .isActive(isActive)
                .build();
    }

    public static ServiceEntity fromDomain(ServiceDto dto) {
        return ServiceEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .isActive(dto.getIsActive())
                .build();
    }
}

