package com.agendaService.infrastructure.repositories.user.persistent;

import com.agendaService.domain.user.dtos.Covenant;
import com.agendaService.domain.user.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String id;
    @Indexed(background = true)
    private String email;
    @Indexed(background = true)
    private String name;
    private String phone;
    @Builder.Default
    private Boolean isActive = true;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Indexed(background = true)
    private List<Covenant> covenants;

    public UserDto toDomain(){
        return UserDto.builder()
                .id(id)
                .email(email)
                .name(name)
                .phone(phone)
                .isActive(isActive)
                .createdAt(createdAt)
                .covenants(covenants)
                .build();
    }
}