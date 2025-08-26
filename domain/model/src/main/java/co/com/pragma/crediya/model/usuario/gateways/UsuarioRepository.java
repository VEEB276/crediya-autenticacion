package co.com.pragma.crediya.model.usuario.gateways;

import co.com.pragma.crediya.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> saveUser(Usuario usuario);
    Mono<Usuario> findByCorreo(String correo);
    Flux<Usuario> getAllUsers();
    Mono<Usuario> getUserById(Long id);
    Mono<Usuario> editUser(Usuario usuario);
    Mono<Void> deleteUser(Long id);
}
