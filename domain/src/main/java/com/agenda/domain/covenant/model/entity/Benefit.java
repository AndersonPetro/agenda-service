package com.agenda.domain.covenant.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Benefit {
    private Float discountAmount;
    private String discountDescription;
}
