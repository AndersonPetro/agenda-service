package com.agendaService.contract.v1;

import com.agendaService.contract.v1.request.CreateAppointmentRequest;
import com.agendaService.contract.v1.request.UpdateAppointmentRequest;
import com.agendaService.contract.v1.response.AppointmentResponse;
import com.agendaService.contract.v1.response.TimeSlotResponse;
import com.agendaService.domain.appointment.port.api.AppointmentApiPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/agenda/appointments")
@AllArgsConstructor
@Tag(name = "Agendamentos", description = "Agenda digital com horários disponíveis e agendamentos")
public class AppointmentController {

    private final AppointmentApiPort appointmentApiPort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar novo agendamento")
    public Mono<AppointmentResponse> create(@RequestBody @Valid CreateAppointmentRequest request) {
        return appointmentApiPort.create(request.toInput())
                .map(AppointmentResponse::fromDomain);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID")
    public Mono<AppointmentResponse> findById(@PathVariable String id) {
        return appointmentApiPort.findById(id)
                .map(AppointmentResponse::fromDomain);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Listar agendamentos de um usuário")
    public Flux<AppointmentResponse> findByUserId(@PathVariable("userId") String userId) {
        return appointmentApiPort.findByUserId(userId)
                .map(AppointmentResponse::fromDomain);
    }

    @GetMapping
    @Operation(summary = "Listar todos os agendamentos")
    public Flux<AppointmentResponse> findAll() {
        return appointmentApiPort.findAll()
                .map(AppointmentResponse::fromDomain);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar agendamento")
    public Mono<AppointmentResponse> update(@PathVariable String id,
                                             @RequestBody @Valid UpdateAppointmentRequest request) {
        return appointmentApiPort.update(id, request.toInput())
                .map(AppointmentResponse::fromDomain);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelar agendamento")
    public Mono<AppointmentResponse> cancel(@PathVariable String id) {
        return appointmentApiPort.cancel(id)
                .map(AppointmentResponse::fromDomain);
    }

    @PatchMapping("/{id}/confirm")
    @Operation(summary = "Confirmar agendamento")
    public Mono<AppointmentResponse> confirm(@PathVariable String id) {
        return appointmentApiPort.confirm(id)
                .map(AppointmentResponse::fromDomain);
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Marcar agendamento como concluído")
    public Mono<AppointmentResponse> complete(@PathVariable String id) {
        return appointmentApiPort.complete(id)
                .map(AppointmentResponse::fromDomain);
    }

    @GetMapping("/slots")
    @Operation(summary = "Consultar horários disponíveis para um serviço em uma data")
    public Flux<TimeSlotResponse> findAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String serviceId) {
        return appointmentApiPort.findAvailableSlots(date, serviceId)
                .map(TimeSlotResponse::fromDomain);
    }
}

