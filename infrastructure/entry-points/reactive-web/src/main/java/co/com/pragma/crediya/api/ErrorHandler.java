package co.com.pragma.crediya.api;

import co.com.pragma.crediya.exception.BusinessException;
import co.com.pragma.crediya.exception.ValidationException;
import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;


public class ErrorHandler {

    public static Mono<ServerResponse> handleError(Throwable error) {
        if (error instanceof ValidationException) {
            return ServerResponse.badRequest()
                    .bodyValue(new ErrorResponse(400, error.getMessage(), null, LocalDateTime.now()));
        }
        if (error instanceof BusinessException) {
            return ServerResponse.status(HttpStatus.CONFLICT)
                    .bodyValue(new ErrorResponse(409, error.getMessage(), null, LocalDateTime.now()));
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(new ErrorResponse(500, "Error inesperado", null, LocalDateTime.now()));
    }

    private ErrorHandler() { }
}
