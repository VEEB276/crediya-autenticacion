package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.r2dbc.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        Long id = 1L;
        UsuarioEntity ue = UsuarioEntity.builder()
                .idUsuario(id)
                .nombre("John")
                .apellido("Doe")
                .build();
        Usuario u = Usuario.builder()
                .idUsuario(id)
                .nombre("John")
                .apellido("Doe")
                .build();

        when(repository.findById(id)).thenReturn(Mono.just(ue));
        when(mapper.map(ue, Usuario.class)).thenReturn(u);

        Mono<Usuario> result = repositoryAdapter.findById(id);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UsuarioEntity ue = UsuarioEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        Usuario u = Usuario.builder().idUsuario(1L).nombre("John").apellido("Doe").build();

        when(repository.findAll()).thenReturn(Flux.just(ue));
        when(mapper.map(ue, Usuario.class)).thenReturn(u);

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        Usuario example = Usuario.builder().nombre("John").build();
        UsuarioEntity ueExample = UsuarioEntity.builder().nombre("John").build();
        UsuarioEntity ue = UsuarioEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        Usuario u = Usuario.builder().idUsuario(1L).nombre("John").apellido("Doe").build();

        when(mapper.map(example, UsuarioEntity.class)).thenReturn(ueExample);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(ue));
        when(mapper.map(ue, Usuario.class)).thenReturn(u);

        Flux<Usuario> result = repositoryAdapter.findByExample(example);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        Usuario u = Usuario.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        UsuarioEntity ue = UsuarioEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();

        when(mapper.map(u, UsuarioEntity.class)).thenReturn(ue);
        when(repository.save(ue)).thenReturn(Mono.just(ue));
        when(mapper.map(ue, Usuario.class)).thenReturn(u);

        Mono<Usuario> result = repositoryAdapter.save(u);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();
    }

    /*@Test
    void mustSaveUserSuccessfully() {
        Usuario u = Usuario.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        UsuarioEntity ue = UsuarioEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();

        when(mapper.map(u, UsuarioEntity.class)).thenReturn(ue);
        when(repository.save(ue)).thenReturn(Mono.just(ue));
        when(mapper.map(ue, Usuario.class)).thenReturn(u);

        Mono<Usuario> result = repositoryAdapter.saveUser(u);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();

        verify(repository).save(ue);
    }*/

    /*@Test
    void mustLogErrorWhenSaveUserFails() {
        Usuario u = Usuario.builder().idUsuario(1L).nombre("John").apellido("Doe").build();
        UsuarioEntity ue = UsuarioEntity.builder().idUsuario(1L).nombre("John").apellido("Doe").build();

        when(mapper.map(u, UsuarioEntity.class)).thenReturn(ue);
        when(repository.save(ue)).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<Usuario> result = repositoryAdapter.saveUser(u);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(repository).save(ue);
    }*/

    @Test
    void mustFindByCorreo() {
        String correo = "john@example.com";
        Usuario u = Usuario.builder()
                .idUsuario(1L)
                .nombre("John")
                .apellido("Doe")
                .correoElectronico(correo)
                .build();

        when(repository.findByCorreoElectronico(correo)).thenReturn(Mono.just(u));

        Mono<Usuario> result = repositoryAdapter.findByCorreo(correo);

        StepVerifier.create(result)
                .expectNext(u)
                .verifyComplete();

        verify(repository).findByCorreoElectronico(correo);
    }

}
