package co.com.pragma.crediya.usecase.usuario;

import co.com.pragma.crediya.exception.ValidationException;
import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioUseCaseTest {

    UsuarioRepository usuarioRepository;
    UsuarioUseCase usuarioUseCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        usuarioUseCase = new UsuarioUseCase(usuarioRepository);
    }

    @Test
    void saveUserSuccess() {
        Usuario usuario = new Usuario(null, "Val", "Escobar", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar2@gmail.com", BigDecimal.valueOf(1000), "123456", "secret", 1L);

        when(usuarioRepository.findByCorreo(usuario.getCorreoElectronico()))
                .thenReturn(Mono.empty());
        when(usuarioRepository.saveUser(usuario))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectNext(usuario)
                .verifyComplete();

        verify(usuarioRepository).findByCorreo(usuario.getCorreoElectronico());
        verify(usuarioRepository).saveUser(usuario);
    }

    @Test
    void saveUserSalarioMenorACero() {
        Usuario usuario = new Usuario(null, "Val", "Escobar", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar2@gmail.com", BigDecimal.valueOf(-1000), "123456", "secret", 1L);

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().contains("El salario base debe estar entre 0"))
                .verify();
    }

    @Test
    void saveUserSalarioMayorAlLimite() {
        Usuario usuario = new Usuario(null, "Val", "Escobar", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar2@gmail.com", BigDecimal.valueOf(100000000), "123456", "secret", 1L);

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().contains("El salario base debe estar entre 0"))
                .verify();
    }

    /*@Test
    void saveUserCorreoYaRegistrado() {
        Usuario usuario = new Usuario(null, "Val", "Escobar", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar@gmail.com", BigDecimal.valueOf(1000), "123456", "secret", 1L);

        when(usuarioRepository.findByCorreo(usuario.getCorreoElectronico()))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.saveUser(usuario))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().contains("El correo ya se encuentra registrado"))
                .verify();

        verify(usuarioRepository).findByCorreo(usuario.getCorreoElectronico());
        verify(usuarioRepository, never()).saveUser(any());
    }*/

    @Test
    void findByDocumentSuccess() {
        Usuario usuario = new Usuario(null, "Val", "Escobar", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar2@gmail.com", BigDecimal.valueOf(1000), "1234", "secret", 1L);

        when(usuarioRepository.findByDocumentoIdentidad("1234"))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.findByDocument("1234"))
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void findByDocument_NotFound() {
        when(usuarioRepository.findByDocumentoIdentidad("9999"))
                .thenReturn(Mono.empty());

        StepVerifier.create(usuarioUseCase.findByDocument("9999"))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().contains("No existe usuario con el documento ingresado"))
                .verify();
    }

    @Test
    void findByEmailSuccess() {
        Usuario usuario = new Usuario(null, "Val", "Escobar", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar2@gmail.com", BigDecimal.valueOf(1000), "123456", "secret", 1L);

        when(usuarioRepository.findByCorreo("valentina.escobar2@gmail.com"))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(usuarioUseCase.findByEmail("valentina.escobar2@gmail.com"))
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void findByEmailNotFound() {
        when(usuarioRepository.findByCorreo("inexistente@test.com"))
                .thenReturn(Mono.empty());

        StepVerifier.create(usuarioUseCase.findByEmail("inexistente@test.com"))
                .expectErrorMatches(e -> e instanceof ValidationException &&
                        e.getMessage().contains("No existe usuario con el correo ingresado"))
                .verify();
    }

}
