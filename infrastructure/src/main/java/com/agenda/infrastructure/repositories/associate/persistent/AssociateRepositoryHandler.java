package com.agenda.infrastructure.repositories.associate.persistent;

import com.agenda.infrastructure.repositories.associate.entity.AssociateCovenantResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface AssociateRepositoryHandler {
    Mono<AssociateCovenantResult> findAssociateByCpfAndCovenantCode(String cpf, Long covenantCode);
    Mono<AssociateCovenantResult> findAssociateByRegistrationAndCovenantCode(String registration, Long covenantCode);
    Mono<Void> updateAssociate(String cpf, Map<String, Object> params);
    Mono<AssociateCovenantResult> save(AssociateCovenantResult associateCovenantResult);
    Flux<AssociateCovenantResult> findWithDependentsByRegistrationAndCovenantCode(String registration, Long covenantCode);
    Mono<Long> countAssociatesByCovenantCode(Long covenantCode);

}
