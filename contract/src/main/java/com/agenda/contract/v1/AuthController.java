package com.agenda.contract.v1;

import com.agenda.contract.v1.request.LoginRequest;
import com.agenda.contract.v1.request.SignupRequest;
import com.agenda.domain.auth.port.api.AuthApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/agenda/auth")
@AllArgsConstructor
@Tag(name = "Autenticação", description = "Cadastro e autenticação de usuários")
public class AuthController {

    private final AuthApiPort authApiPort;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário")
    public Mono<Map<String, Object>> login(@RequestBody @Valid LoginRequest request) {
        return authApiPort.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo usuário")
    public Mono<Map<String, String>> signup(@RequestBody @Valid SignupRequest request) {
        return authApiPort.signup(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        ).map(userId -> Map.of(
                "userId", userId,
                "message", "Usuário cadastrado com sucesso"
        ));
    }
}

