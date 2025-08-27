package co.com.pragma.crediya.api;

import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class Handler {

    private  final UsuarioUseCase usuarioUseCase;

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        log.info("Inicio de la petición para guardar usuario");

        return serverRequest.bodyToMono(Usuario.class)
                .doOnNext(usuario -> log.info("Usuario recibido: {}", usuario))
                .flatMap(usuarioUseCase::saveUser)
                .doOnNext(savedUser -> log.info("Usuario guardado con éxito: {}", savedUser))
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser)
                )
                .doOnError(e -> log.error("Error al guardar usuario", e))
                .onErrorResume(ErrorHandler::handleError)
                .doFinally(signal -> log.info("Fin de la petición para guardar usuario"));
    }
}