package com.agendaService.domain.user;

import com.agendaService.domain.covenant.model.dtos.CovenantDto;
import com.agendaService.domain.covenant.port.api.CovenantApiPort;
import com.agendaService.domain.input.SaveUserInput;
import com.agendaService.domain.user.dtos.Covenant;
import com.agendaService.domain.user.port.api.UserApiPort;
import com.agendaService.domain.user.dtos.UserDto;
import com.agendaService.domain.user.port.spi.OauthServerSpiPort;
import com.agendaService.domain.user.port.spi.UserSpiPort;
import com.common.exception.AgendaHttpException;
import com.common.exception.ExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService implements UserApiPort {

    private final UserSpiPort userSpiPort;
    private final OauthServerSpiPort oauthServerSpiPort;
    private final CovenantApiPort covenantApiPort;

    @Override
    public Mono<UserDto> save(SaveUserInput saveUserInput) {
        return oauthServerSpiPort.findUserWithCriteria(saveUserInput.getFullName(), saveUserInput.getEmail())
                .flatMap(userId -> updateUser(userId, saveUserInput))
                .switchIfEmpty(Mono.defer(() -> createUser(saveUserInput)));
    }

    @Override
    public Mono<UserDto> findById(String id) {
        return userSpiPort.findById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        ExceptionUtils.notFoundException("Usuário não encontrado.")
                )));
    }

    @Override
    public Mono<Page<UserDto>> findByFilters(PageRequest pageRequest, Map<String, String> filters) {
        return userSpiPort.findByFilters(pageRequest, filters);
    }

    @Override
    public Mono<UserDto> setUserActive(String id, Boolean isActive) {
        return userSpiPort.setUserActive(id, isActive)
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        ExceptionUtils.notFoundException("Usuário não encontrado.")
                )));
    }




    private Mono<UserDto> updateUser(String userId, SaveUserInput saveUserInput) {
       return findCovenant(saveUserInput.getCovenants())
               .onErrorMap(AgendaHttpException.class, e ->
                       AgendaHttpException.withHttpStatus(e.getHttpStatus())
                               .withMessage("convênio não encontrado" + saveUserInput.getCovenants())
                               .withDetail("error", e.getMessage())
                               .build())
               .flatMap(covenants ->
                       oauthServerSpiPort.updateUser(userId, saveUserInput)
                               .then(Mono.just(covenants))
               )
               .flatMap(covenants ->
                       userSpiPort.save(UserDto.builder()
                               .id(userId)
                               .email(saveUserInput.getEmail())
                               .name(saveUserInput.getFullName())
                               .isActive(true)
                               .covenants(covenants.stream()
                                       .map(covenant -> Covenant.builder()
                                               .id(covenant.getId())
                                               .code(covenant.getCode())
                                               .name(covenant.getName())
                                               .build())
                                       .toList()
                               )
                               .build()
                       ));
    }

    private Mono<List<CovenantDto>> findCovenant(List<Long> covenants) {
        return covenantApiPort.findByCodeIn(covenants)
                .collectList();

    }

    private Mono<UserDto> createUser(SaveUserInput saveUserInput) {
        var user = UserDto.builder()
                .email(saveUserInput.getEmail())
                .name(saveUserInput.getFullName())
                .isActive(true)
                .build();
        return userSpiPort.save(user);
    }

    @Override
    public Mono<UserDto> update(String id, UserDto userDto) {
        return userSpiPort.findById(id)
                .flatMap(existing -> {
                    existing.setName(userDto.getName());
                    existing.setEmail(userDto.getEmail());
                    existing.setPhone(userDto.getPhone());
                    return userSpiPort.save(existing);
                })
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        com.common.exception.ExceptionUtils.notFoundException("Usuário não encontrado.")
                )));
    }
}
