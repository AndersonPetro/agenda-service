package com.agenda.contract.v1;

import com.agenda.contract.v1.request.SaveUserRequest;
import com.agenda.contract.v1.response.SignupResponse;
import com.agenda.domain.user.port.api.UserApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/agenda")
@AllArgsConstructor
public class AgendaController {

    private final UserApiPort userApiPort;


    @PutMapping
    @Operation(
            summary = "Criar/atualizar usuário"
    )
    @ApiResponse
    @PreAuthorize("hasRole('admin')")
    public Flux<SignupResponse> save(@RequestBody @Valid List<SaveUserRequest> saveUserRequest) {
        return Flux.fromIterable(saveUserRequest.stream()
                        .map(SaveUserRequest::toInput)
                        .toList())
                .flatMap(userApiPort::save)
                .map(input -> SignupResponse.builder()
                        .email(input.getEmail())
                        .name(input.getName())
                        .build()
                );
    }

}