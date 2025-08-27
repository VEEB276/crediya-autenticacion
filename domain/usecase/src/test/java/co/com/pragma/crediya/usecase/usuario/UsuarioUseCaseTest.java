package co.com.pragma.crediya.usecase.usuario;

import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class UsuarioUseCaseTest {

    UsuarioRepository usuarioRepository;
    UsuarioUseCase usuarioUseCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        usuarioUseCase = new UsuarioUseCase(usuarioRepository);
    }

    @Test
    void saveUser_nombreNulo_lanzaValidationException() {
        Usuario usuario = new Usuario(null, "Apellido", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El nombre es obligatorio")
                .verify();
    }

    @Test
    void saveUser_apellidoNulo_lanzaValidationException() {
        Usuario usuario = new Usuario("Valentina", null, "valentina.escobar@gmail.com", BigDecimal.valueOf(1000));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El apellido es obligatorio")
                .verify();
    }

    @Test
    void saveUser_correoNulo_lanzaValidationException() {
        Usuario usuario = new Usuario("Valentina", "Escobar", null, BigDecimal.valueOf(1000));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El correo electrónico es obligatorio")
                .verify();
    }

    @Test
    void saveUser_salarioNegativo_lanzaValidationException() {
        Usuario usuario = new Usuario("Valentina", "Escobar", "valentina.escobar@gmail.com", BigDecimal.valueOf(-10));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El salario base debe estar entre 0 y 15'000.000")
                .verify();
    }

    @Test
    void saveUser_correoExistente_lanzaValidationException() {
        Usuario usuario = new Usuario("Valentina", "Escobar", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000));

        Mockito.when(usuarioRepository.findByCorreo("valentina.escobar@gmail.com"))
                .thenReturn(Mono.just(usuario));

        Mockito.when(usuarioRepository.saveUser(Mockito.any()))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El correo ya se encuentra registrado")
                .verify();
    }

    @Test
    void saveUser_valido_guardaUsuario() {
        Usuario usuario = new Usuario("Valentina", "Escobar", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000));

        Mockito.when(usuarioRepository.findByCorreo("valentina.escobar@gmail.com"))
                .thenReturn(Mono.empty());
        Mockito.when(usuarioRepository.saveUser(usuario))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectNext(usuario)
                .verifyComplete();
    }


}
