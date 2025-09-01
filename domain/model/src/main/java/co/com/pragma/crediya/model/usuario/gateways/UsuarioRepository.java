package co.com.pragma.crediya.model.usuario.gateways;

import co.com.pragma.crediya.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> saveUser(Usuario usuario);
    Mono<Usuario> findByCorreo(String correo);
    Mono<Usuario> findByDocumentoIdentidad(String documento);
}
