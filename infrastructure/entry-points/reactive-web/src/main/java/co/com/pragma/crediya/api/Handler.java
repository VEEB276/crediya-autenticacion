package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.LoginDTO;
import co.com.pragma.crediya.api.dto.ResponseLoginDTO;
import co.com.pragma.crediya.api.mapper.UserDtoMapper;
import co.com.pragma.crediya.exception.ValidationException;
import co.com.pragma.crediya.usecase.usuario.UsuarioUseCase;
import co.com.pragma.crediya.usecase.usuario.login.LoginUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private  final UsuarioUseCase usuarioUseCase;

    private  final LoginUseCase loginUseCase;

    private final UserDtoMapper mapper;

    private final Validator validator;

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        log.info("Inicio de la petición para guardar usuario");

        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(usuario -> log.info("Usuario recibido: {}", usuario))
                .flatMap(dto -> {
                    Set<ConstraintViolation<CreateUserDTO>> violations =  validator.validate(dto);
                    if (!violations.isEmpty()) {
                        return Mono.error(new ValidationException(violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "))));
                    }
                    return Mono.just(dto);
                })
                .map(mapper::toModel)
                .flatMap(usuarioUseCase::saveUser)
                .doOnNext(savedUser -> log.info("Usuario guardado con éxito: {}", savedUser))
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapper.toResponse(savedUser))
                )
                .doOnError(e -> log.error("Error al guardar usuario", e))
                .onErrorResume(ErrorHandler::handleError)
                .doFinally(signal -> log.info("Fin de la petición para guardar usuario"));
    }

    public Mono<ServerResponse> listenGetUserByDocument(ServerRequest request) {
        log.info("Inicio de la petición para obtener usuario por documento");

        String documento = request.pathVariable("documento");

        return usuarioUseCase.findByDocument(documento)
                .doOnNext(usuario -> log.info("Usuario encontrado: {}", usuario))
                .flatMap(usuario -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(mapper.toResponse(usuario))
                )
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(e -> log.error("Error al obtener usuario por documento", e))
                .onErrorResume(ErrorHandler::handleError)
                .doFinally(signal -> log.info("Fin de la petición para obtener usuario por documento"));
    }

    public Mono<ServerResponse> login(ServerRequest request) {

        return request.bodyToMono(LoginDTO.class)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(loginUseCase.login(dto.correoElectronico(), dto.password()), ResponseLoginDTO.class)
                        .onErrorResume(ErrorHandler::handleError));
    }
}