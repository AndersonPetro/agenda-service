package com.agenda.infrastructure.adapters;

import com.agenda.domain.appointment.dtos.AppointmentDto;
import com.agenda.domain.appointment.port.spi.AppointmentSpiPort;
import com.agenda.infrastructure.repositories.appointment.persistent.AppointmentEntity;
import com.agenda.infrastructure.repositories.appointment.persistent.AppointmentRepository;
import com.common.enums.AppointmentStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AppointmentSpiImpl implements AppointmentSpiPort {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Mono<AppointmentDto> save(AppointmentDto appointmentDto) {
        return appointmentRepository.save(AppointmentEntity.fromDomain(appointmentDto))
                .map(AppointmentEntity::toDomain);
    }

    @Override
    public Mono<AppointmentDto> findById(String id) {
        return appointmentRepository.findById(id)
                .map(AppointmentEntity::toDomain);
    }

    @Override
    public Flux<AppointmentDto> findByUserId(String userId) {
        return appointmentRepository.findByUserId(userId)
                .map(AppointmentEntity::toDomain);
    }

    @Override
    public Flux<AppointmentDto> findAll() {
        return appointmentRepository.findAll()
                .map(AppointmentEntity::toDomain);
    }

    @Override
    public Flux<AppointmentDto> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByScheduledAtBetween(start, end)
                .map(AppointmentEntity::toDomain);
    }

    @Override
    public Flux<AppointmentDto> findByStatus(AppointmentStatusEnum status) {
        return appointmentRepository.findByStatus(status)
                .map(AppointmentEntity::toDomain);
    }

    @Override
    public Flux<AppointmentDto> findByStatusAndScheduledAtBetween(AppointmentStatusEnum status, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByStatusAndScheduledAtBetween(status, start, end)
                .map(AppointmentEntity::toDomain);
    }
}

