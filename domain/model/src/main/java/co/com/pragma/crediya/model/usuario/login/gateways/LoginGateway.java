package co.com.pragma.crediya.model.usuario.login.gateways;

import co.com.pragma.crediya.model.usuario.login.Login;
import reactor.core.publisher.Mono;

public interface LoginGateway {

    Mono<Login> login(String email, String password);
}
