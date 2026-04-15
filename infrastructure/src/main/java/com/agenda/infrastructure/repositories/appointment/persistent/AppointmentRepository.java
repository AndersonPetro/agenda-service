package com.agenda.infrastructure.repositories.appointment.persistent;

import com.common.enums.AppointmentStatusEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface AppointmentRepository extends ReactiveMongoRepository<AppointmentEntity, String> {
    Flux<AppointmentEntity> findByUserId(String userId);
    Flux<AppointmentEntity> findByScheduledAtBetween(LocalDateTime start, LocalDateTime end);
    Flux<AppointmentEntity> findByStatus(AppointmentStatusEnum status);
    Flux<AppointmentEntity> findByStatusAndScheduledAtBetween(AppointmentStatusEnum status, LocalDateTime start, LocalDateTime end);
}

