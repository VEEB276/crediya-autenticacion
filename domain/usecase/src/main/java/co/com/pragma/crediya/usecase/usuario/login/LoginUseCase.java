package co.com.pragma.crediya.usecase.usuario.login;

import co.com.pragma.crediya.model.usuario.login.gateways.LoginGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {

    private final LoginGateway loginGateway;

    public Mono<String> login(String email, String password){
        return loginGateway.login(email, password);
    }

}
