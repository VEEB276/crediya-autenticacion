package co.com.pragma.crediya.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springdoc.core.annotations.RouterOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperation(
            path = "/api/v1/usuarios",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.POST,
            beanClass = Handler.class,
            beanMethod = "listenSaveUser",
            operation = @Operation(
                    operationId = "guardarUsuario",
                    description = "Guarda un nuevo usuario",
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Usuario guardado correctamente"),
                            @ApiResponse(responseCode = "400", description = "Error de validación")
                    }
            )
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::listenSaveUser);
    }
}
