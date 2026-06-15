package com.agendaService.domain.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String name;
    private String phone;
    private Boolean isActive;
    private Instant createdAt;
    private List<Covenant> covenants;
}

