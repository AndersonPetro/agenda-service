package com.agendaService.contract.v1;

import com.agendaService.contract.v1.response.UserResponse;
import com.agendaService.domain.user.port.api.UserApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/agenda/users")
@AllArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários e clientes")
public class UserController {

    private final UserApiPort userApiPort;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuários com filtros")
    public Mono<Page<UserResponse>> findByFilters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Map<String, String> filters) {
        
        var queryFilters = new java.util.HashMap<>(filters);
        queryFilters.remove("page");
        queryFilters.remove("size");

        return userApiPort.findByFilters(PageRequest.of(page, size), queryFilters)
                .map(pageDto -> pageDto.map(UserResponse::fromDomain));
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ativar ou desativar usuário")
    public Mono<UserResponse> setUserActive(
            @PathVariable String id,
            @RequestParam Boolean isActive) {
        return userApiPort.setUserActive(id, isActive)
                .map(UserResponse::fromDomain);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @Operation(summary = "Atualizar dados cadastrais do usuário")
    public Mono<UserResponse> updateUser(
            @PathVariable String id,
            @RequestBody UserResponse request) {
        
        var userDto = com.agendaService.domain.user.dtos.UserDto.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .build();
                
        return userApiPort.update(id, userDto)
                .map(UserResponse::fromDomain);
    }
}
