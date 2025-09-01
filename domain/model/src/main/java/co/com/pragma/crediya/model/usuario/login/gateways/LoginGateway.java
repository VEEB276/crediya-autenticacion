package co.com.pragma.crediya.model.usuario.login.gateways;

import reactor.core.publisher.Mono;

public interface LoginGateway {

    Mono<String> login(String email, String password);
}
