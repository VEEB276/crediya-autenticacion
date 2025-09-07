package co.com.pragma.crediya.usecase.rol;

import co.com.pragma.crediya.exception.ValidationException;
import co.com.pragma.crediya.model.rol.Rol;
import co.com.pragma.crediya.model.rol.gateways.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RolUseCaseTest {

    private RolRepository rolRepository;
    private RolUseCase rolUseCase;

    @BeforeEach
    void setUp() {
        rolRepository = Mockito.mock(RolRepository.class);
        rolUseCase = new RolUseCase(rolRepository);
    }

    @Test
    void findById_exitoso() {
        Rol rol = new Rol(1L, "ADMIN", "administradora");

        when(rolRepository.findById(1L)).thenReturn(Mono.just(rol));

        StepVerifier.create(rolUseCase.findById(1L))
                .expectNext(rol)
                .verifyComplete();

        verify(rolRepository).findById(1L);
    }

    @Test
    void findById_noExiste() {
        when(rolRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(rolUseCase.findById(99L))
                .expectErrorMatches(ex -> ex instanceof ValidationException
                        && ex.getMessage().equals("No existe rol con el identificador ingresado"))
                .verify();

        verify(rolRepository).findById(99L);
    }

}
