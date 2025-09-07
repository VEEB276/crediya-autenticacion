package co.com.pragma.crediya.api;

import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.usecase.usuario.UsuarioUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UsuarioUseCase usuarioUseCase;

    /*@Test
    void testListenPOSTUseCase() {
        Usuario usuario = new Usuario(null, "Val", "Escobar", "27-06-2001", "Calle 48 #27",
                "312121212", "valentina.escobar2@gmail.com", BigDecimal.valueOf(1000), "123456", "secret", 1L);

        Mockito.when(usuarioUseCase.saveUser(Mockito.any()))
                .thenReturn(Mono.just(usuario));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(usuario)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Usuario.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.getNombre()).isEqualTo("Valen");
                            Assertions.assertThat(userResponse.getCorreoElectronico()).isEqualTo("valen@gmail.com");
                        }
                );
    }*/
}
