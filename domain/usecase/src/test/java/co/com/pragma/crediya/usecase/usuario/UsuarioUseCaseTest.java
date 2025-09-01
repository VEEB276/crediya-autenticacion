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
    void saveUserNombreNulo() {
        Usuario usuario = new Usuario(null, null, "Apellido", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000), "123456", 1L);

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El nombre es obligatorio")
                .verify();
    }

    @Test
    void saveUserApellidoNulo() {
        Usuario usuario = new Usuario(null, "Valentina", null, "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000), "123456", 1L);

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El apellido es obligatorio")
                .verify();
    }

    @Test
    void saveUserCorreoNulo() {
        Usuario usuario = new Usuario(null, "Valentina", "Apellido", "27-06-2001", "Calle 48 #27",
                "312121212", null, BigDecimal.valueOf(1000), "123456", 1L);

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El correo electrónico es obligatorio")
                .verify();
    }

    @Test
    void saveUserSalarioNegativo() {
        Usuario usuario = new Usuario(null, "Valentina", "Apellido", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar@gmail.com", BigDecimal.valueOf(-1000), "123456", 1L);

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El salario base debe estar entre 0 y 15'000.000")
                .verify();
    }

    @Test
    void saveUserCorreoExistente() {
        Usuario usuario = new Usuario(null, "Valentina", "Apellido", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000), "123456", 1L);

        Mockito.when(usuarioRepository.findByCorreo("valentina.escobar@gmail.com"))
                .thenReturn(Mono.just(usuario));

        Mockito.when(usuarioRepository.saveUser(Mockito.any()))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMessage("El correo ya se encuentra registrado")
                .verify();
    }

    @Test
    void saveUserValido() {
        Usuario usuario = new Usuario(null, "Valentina", "Apellido", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000), "123456", 1L);

        Mockito.when(usuarioRepository.findByCorreo("valentina.escobar@gmail.com"))
                .thenReturn(Mono.empty());
        Mockito.when(usuarioRepository.saveUser(usuario))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectNext(usuario)
                .verifyComplete();
    }


}
