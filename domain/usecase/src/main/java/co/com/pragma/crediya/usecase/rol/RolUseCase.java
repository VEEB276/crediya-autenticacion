package co.com.pragma.crediya.usecase.rol;

import co.com.pragma.crediya.exception.ValidationException;
import co.com.pragma.crediya.model.rol.Rol;
import co.com.pragma.crediya.model.rol.gateways.RolRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RolUseCase {

    private final RolRepository rolRepository;

    public Mono<Rol> findById(Long id) {
        return rolRepository.findById(id)
                .switchIfEmpty(Mono.error(new ValidationException("No existe rol con el identificador ingresado")));
    }

}
