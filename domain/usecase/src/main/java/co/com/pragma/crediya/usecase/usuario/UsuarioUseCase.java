package co.com.pragma.crediya.usecase.usuario;

import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> saveUser(Usuario user) {
        return usuarioRepository.saveUser(user);
    }

}
