package co.com.pragma.crediya.api.errors;

import co.com.pragma.crediya.api.ErrorDetail;
import co.com.pragma.crediya.api.ErrorAllResponse;
import co.com.pragma.crediya.exception.BusinessException;
import co.com.pragma.crediya.exception.ValidationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;
import reactor.core.publisher.Mono;

import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@lombok.extern.slf4j.Slf4j
public class GlobalErrorHandler {

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler globalExceptionHandler(ErrorAttributes errorAttributes,
                                                           ApplicationContext applicationContext,
                                                           ServerCodecConfigurer codecConfigurer) {
        return new GlobalErrorWebExceptionHandler(errorAttributes, applicationContext, codecConfigurer);
    }

    static class GlobalErrorWebExceptionHandler extends org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler {

        GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                       ApplicationContext applicationContext,
                                       ServerCodecConfigurer codecConfigurer) {
            super(errorAttributes, new WebProperties.Resources(), applicationContext);
            super.setMessageReaders(codecConfigurer.getReaders());
            super.setMessageWriters(codecConfigurer.getWriters());
        }

        @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
        }

        private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
            Throwable ex = getError(request);
            String correlationId = request.headers().firstHeader("X-Correlation-Id");

            HttpStatus status;
            ErrorAllResponse payload;

            if (ex instanceof BusinessException be) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorAllResponse.of(
                        "BUSINESS_ERROR",
                        be.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else if (ex instanceof ValidationException ve) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorAllResponse.of(
                        "VALIDATION_ERROR",
                        ve.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else if (ex instanceof ConstraintViolationException cve) {
                status = HttpStatus.BAD_REQUEST;
                List<ErrorDetail> details = cve.getConstraintViolations().stream()
                        .map(v -> new ErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
                        .toList();
                payload = ErrorAllResponse.of(
                        "VALIDATION_ERROR",
                        "Datos de entrada inválidos",
                        status.value(),
                        request.path(),
                        details,
                        correlationId
                );
            } else if (ex instanceof IllegalArgumentException iae) {
                status = HttpStatus.BAD_REQUEST;
                payload = ErrorAllResponse.of(
                        "INVALID_ARGUMENT",
                        iae.getMessage(),
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                payload = ErrorAllResponse.of(
                        "INTERNAL_ERROR",
                        "Ocurrió un error inesperado",
                        status.value(),
                        request.path(),
                        null,
                        correlationId
                );
            }

            return ServerResponse.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload));
        }
    }
}