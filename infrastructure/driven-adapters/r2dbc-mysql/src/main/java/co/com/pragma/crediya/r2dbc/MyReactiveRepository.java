package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.r2dbc.entities.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, Long>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<Usuario> findByCorreoElectronico(String correoElectronico);

    Mono<Usuario> findByDocumentoIdentidad(String documentoIdentidad);

}
