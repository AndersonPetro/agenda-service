package com.agenda.infrastructure.repositories.associate.persistent;

import com.agenda.infrastructure.repositories.associate.entity.AssociateEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AssociateRepository extends ReactiveMongoRepository<AssociateEntity, String>, AssociateRepositoryHandler {
    Mono<AssociateEntity> findByCpf(String cpf);

    Mono<Boolean> existsByCpf(String cpf);

    Mono<AssociateEntity> findByCovenantsCardCardNumberAndCovenantsCardEncryptedPassword(String cardNumber, String encryptedPassword);
}
