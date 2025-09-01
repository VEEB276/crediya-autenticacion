package co.com.pragma.crediya.usecase.usuario;

import co.com.pragma.crediya.exception.ValidationException;
import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> saveUser(Usuario usuario) {

        if (usuario.getSalarioBase().compareTo(BigDecimal.ZERO) < 0
                || usuario.getSalarioBase().compareTo(new BigDecimal("15000000")) > 0) {
            return Mono.error(new ValidationException("El salario base debe estar entre 0 y 15'000.000"));
        }

        return usuarioRepository.findByCorreo(usuario.getCorreoElectronico())
                .flatMap(existing -> Mono.<Usuario>error(
                        new ValidationException("El correo ya se encuentra registrado")
                ))
                .switchIfEmpty(usuarioRepository.saveUser(usuario));
    }

    public Mono<Usuario> findByDocument(String documento) {
        return usuarioRepository.findByDocumentoIdentidad(documento)
                .switchIfEmpty(Mono.error(new ValidationException("No existe usuario con el documento ingresado")));
    }

}
