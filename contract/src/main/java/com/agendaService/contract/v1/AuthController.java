package com.agendaService.contract.v1;

import com.agendaService.contract.v1.request.LoginRequest;
import com.agendaService.contract.v1.request.SignupRequest;
import com.agendaService.contract.v1.request.RecoveryRequest;
import com.agendaService.contract.v1.request.RecoveryConfirmRequest;
import com.agendaService.domain.auth.port.api.AuthApiPort;
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

    /**
     * Cadastro de novos usuários salva Keycloak. Salva retorna o ID do usuário criado e uma mensagem de sucesso.
     */
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

    @PostMapping("/recovery")
    @Operation(summary = "Solicitar recuperação de senha")
    public Mono<Map<String, String>> recovery(@RequestBody @Valid RecoveryRequest request) {
        return authApiPort.requestPasswordRecovery(request.getEmail())
                .then(Mono.fromCallable(() -> {
                    String devCode = authApiPort.getRecoveryCode(request.getEmail());
                    if (devCode != null) {
                        return Map.of(
                                "message", "Instruções de recuperação de senha enviadas com sucesso!",
                                "devCode", devCode
                        );
                    }
                    return Map.of("message", "Instruções de recuperação de senha enviadas com sucesso!");
                }));
    }

    @PostMapping("/recovery/confirm")
    @Operation(summary = "Confirmar recuperação de senha")
    public Mono<Map<String, String>> confirmRecovery(@RequestBody @Valid RecoveryConfirmRequest request) {
        return authApiPort.confirmPasswordRecovery(request.getEmail(), request.getCode(), request.getNewPassword())
                .then(Mono.just(Map.of("message", "Senha alterada com sucesso!")));
    }
}

