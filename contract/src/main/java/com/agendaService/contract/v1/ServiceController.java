package com.agendaService.contract.v1;

import com.agendaService.contract.v1.request.SaveServiceRequest;
import com.agendaService.contract.v1.response.ServiceResponse;
import com.agendaService.domain.service.port.api.ServiceApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/agenda/services")
@AllArgsConstructor
@Tag(name = "Serviços", description = "Cadastro de serviços com descrição, valor e duração")
public class ServiceController {

    private final ServiceApiPort serviceApiPort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo serviço")
    public Mono<ServiceResponse> create(@RequestBody @Valid SaveServiceRequest request) {
        return serviceApiPort.save(request.toInput())
                .map(ServiceResponse::fromDomain);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar serviço existente")
    public Mono<ServiceResponse> update(@PathVariable String id,
                                         @RequestBody @Valid SaveServiceRequest request) {
        return serviceApiPort.update(id, request.toInput())
                .map(ServiceResponse::fromDomain);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviço por ID")
    public Mono<ServiceResponse> findById(@PathVariable String id) {
        return serviceApiPort.findById(id)
                .map(ServiceResponse::fromDomain);
    }

    @GetMapping
    @Operation(summary = "Listar todos os serviços ativos")
    public Flux<ServiceResponse> findAll() {
        return serviceApiPort.findAll()
                .map(ServiceResponse::fromDomain);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desativar serviço")
    public Mono<Void> delete(@PathVariable String id) {
        return serviceApiPort.delete(id);
    }
}

