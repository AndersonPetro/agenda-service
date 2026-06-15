package com.agendaService.domain.appointment;

import com.agendaService.domain.appointment.dtos.AppointmentDto;
import com.agendaService.domain.appointment.dtos.TimeSlotDto;
import com.agendaService.domain.appointment.model.AppointmentInput;
import com.agendaService.domain.appointment.model.UpdateAppointmentInput;
import com.agendaService.domain.appointment.port.api.AppointmentApiPort;
import com.agendaService.domain.appointment.port.spi.AppointmentSpiPort;
import com.agendaService.domain.service.port.spi.ServiceSpiPort;
import com.agendaService.domain.user.port.spi.UserSpiPort;
import com.common.enums.AppointmentStatusEnum;
import com.common.exception.ExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentService implements AppointmentApiPort {

    private static final LocalTime WORK_START = LocalTime.of(8, 0);
    private static final LocalTime WORK_END = LocalTime.of(18, 0);

    private final AppointmentSpiPort appointmentSpiPort;
    private final ServiceSpiPort serviceSpiPort;
    private final UserSpiPort userSpiPort;

    @Override
    public Mono<AppointmentDto> create(AppointmentInput input) {
        return userSpiPort.findById(input.userId())
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Usuário não encontrado")))
                .flatMap(user -> serviceSpiPort.findById(input.serviceId())
                        .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Serviço não encontrado")))
                        .flatMap(service -> {
                            LocalDateTime start = input.scheduledAt();
                            LocalDateTime end = start.plusMinutes(service.getDurationMinutes());

                            return appointmentSpiPort.findByScheduledAtBetween(
                                            start.toLocalDate().atStartOfDay(),
                                            start.toLocalDate().atTime(23, 59, 59))
                                    .filter(existing -> existing.getStatus() != AppointmentStatusEnum.CANCELLED)
                                    .collectList()
                                    .flatMap(existingAppointments -> {
                                        boolean hasConflict = existingAppointments.stream()
                                                .anyMatch(existing -> {
                                                    LocalDateTime existingEnd = existing.getScheduledAt().plusMinutes(service.getDurationMinutes());
                                                    return start.isBefore(existingEnd) && end.isAfter(existing.getScheduledAt());
                                                });

                                        if (hasConflict) {
                                            return Mono.error(ExceptionUtils.badRequest("Horário já está ocupado"));
                                        }

                                        var appointment = AppointmentDto.builder()
                                                .userId(input.userId())
                                                .serviceId(input.serviceId())
                                                .serviceName(service.getName())
                                                .userName(user.getName())
                                                .scheduledAt(input.scheduledAt())
                                                .status(AppointmentStatusEnum.CONFIRMED)
                                                .notes(input.notes())
                                                .createdAt(LocalDateTime.now())
                                                .updatedAt(LocalDateTime.now())
                                                .build();

                                        return appointmentSpiPort.save(appointment);
                                    });
                        })
                );
    }

    @Override
    public Mono<AppointmentDto> findById(String id) {
        return appointmentSpiPort.findById(id)
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Agendamento não encontrado")));
    }

    @Override
    public Flux<AppointmentDto> findByUserId(String userId) {
        return appointmentSpiPort.findByUserId(userId);
    }

    @Override
    public Flux<AppointmentDto> findAll() {
        return appointmentSpiPort.findAll();
    }

    @Override
    public Mono<AppointmentDto> update(String id, UpdateAppointmentInput input) {
        return appointmentSpiPort.findById(id)
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Agendamento não encontrado")))
                .flatMap(existing -> {
                    if (existing.getStatus() == AppointmentStatusEnum.CANCELLED) {
                        return Mono.error(ExceptionUtils.badRequest("Não é possível editar um agendamento cancelado"));
                    }
                    if (existing.getStatus() == AppointmentStatusEnum.COMPLETED) {
                        return Mono.error(ExceptionUtils.badRequest("Não é possível editar um agendamento concluído"));
                    }
                    if (input.scheduledAt() != null) existing.setScheduledAt(input.scheduledAt());
                    if (input.notes() != null) existing.setNotes(input.notes());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return appointmentSpiPort.save(existing);
                });
    }

    @Override
    public Mono<AppointmentDto> cancel(String id) {
        return changeStatus(id, AppointmentStatusEnum.CANCELLED);
    }

    @Override
    public Mono<AppointmentDto> confirm(String id) {
        return changeStatus(id, AppointmentStatusEnum.CONFIRMED);
    }

    @Override
    public Mono<AppointmentDto> complete(String id) {
        return changeStatus(id, AppointmentStatusEnum.COMPLETED);
    }

    @Override
    public Flux<TimeSlotDto> findAvailableSlots(LocalDate date, String serviceId) {
        return serviceSpiPort.findById(serviceId)
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Serviço não encontrado")))
                .flatMapMany(service -> {
                    int duration = service.getDurationMinutes();
                    LocalDateTime dayStart = date.atStartOfDay();
                    LocalDateTime dayEnd = date.atTime(23, 59, 59);

                    return appointmentSpiPort.findByScheduledAtBetween(dayStart, dayEnd)
                            .filter(a -> a.getStatus() != AppointmentStatusEnum.CANCELLED)
                            .collectList()
                            .flatMapMany(existingAppointments -> {
                                Set<LocalTime> occupiedStarts = existingAppointments.stream()
                                        .map(a -> a.getScheduledAt().toLocalTime())
                                        .collect(Collectors.toSet());

                                List<TimeSlotDto> slots = new ArrayList<>();
                                LocalTime current = WORK_START;

                                while (current.plusMinutes(duration).isBefore(WORK_END) || current.plusMinutes(duration).equals(WORK_END)) {
                                    LocalTime slotEnd = current.plusMinutes(duration);

                                    final LocalTime slotStart = current;
                                    boolean isOccupied = existingAppointments.stream()
                                            .anyMatch(existing -> {
                                                LocalTime existingStart = existing.getScheduledAt().toLocalTime();
                                                LocalTime existingEnd = existingStart.plusMinutes(duration);
                                                return slotStart.isBefore(existingEnd) && slotEnd.isAfter(existingStart);
                                            });

                                    slots.add(TimeSlotDto.builder()
                                            .date(date)
                                            .startTime(current)
                                            .endTime(slotEnd)
                                            .available(!isOccupied)
                                            .build());

                                    current = current.plusMinutes(30);
                                }

                                return Flux.fromIterable(slots);
                            });
                });
    }

    private Mono<AppointmentDto> changeStatus(String id, AppointmentStatusEnum newStatus) {
        return appointmentSpiPort.findById(id)
                .switchIfEmpty(Mono.error(ExceptionUtils.notFoundException("Agendamento não encontrado")))
                .flatMap(existing -> {
                    if (existing.getStatus() == AppointmentStatusEnum.CANCELLED) {
                        return Mono.error(ExceptionUtils.badRequest("Agendamento já está cancelado"));
                    }
                    if (existing.getStatus() == AppointmentStatusEnum.COMPLETED && newStatus != AppointmentStatusEnum.COMPLETED) {
                        return Mono.error(ExceptionUtils.badRequest("Agendamento já está concluído"));
                    }
                    existing.setStatus(newStatus);
                    existing.setUpdatedAt(LocalDateTime.now());
                    return appointmentSpiPort.save(existing);
                });
    }
}

