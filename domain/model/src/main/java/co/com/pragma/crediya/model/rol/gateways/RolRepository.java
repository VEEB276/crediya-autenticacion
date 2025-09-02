package co.com.pragma.crediya.model.rol.gateways;

import co.com.pragma.crediya.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {

    Mono<Rol> findById(Long id);

}
