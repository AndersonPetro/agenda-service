package com.agenda.infrastructure.repositories.user.persistent;

import com.agenda.domain.user.dtos.Covenant;
import com.agenda.domain.user.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.User;

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
    @Builder.Default
    private Boolean isActive = true;
    @Indexed(background = true)
    private List<Covenant> covenants;

    public UserDto toDomain(){
        return UserDto.builder()
                .id(id)
                .email(email)
                .name(name)
                .isActive(isActive)
                .covenants(covenants)
                .build();
    }
}