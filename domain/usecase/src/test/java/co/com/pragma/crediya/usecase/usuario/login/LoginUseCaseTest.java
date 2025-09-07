package co.com.pragma.crediya.usecase.usuario.login;

import co.com.pragma.crediya.model.usuario.login.Login;
import co.com.pragma.crediya.model.usuario.login.gateways.LoginGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginUseCaseTest {

    private LoginGateway loginGateway;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        loginGateway = Mockito.mock(LoginGateway.class);
        loginUseCase = new LoginUseCase(loginGateway);
    }

    @Test
    void loginExitoso() {
        String email = "test@example.com";
        String password = "1234";
        Login loginResponse = new Login(email, 3600);

        when(loginGateway.login(email, password)).thenReturn(Mono.just(loginResponse));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectNext(loginResponse)
                .verifyComplete();

        verify(loginGateway).login(email, password);
    }

    @Test
    void loginError() {
        String email = "val@test.com";
        String password = "badpass";

        when(loginGateway.login(email, password))
                .thenReturn(Mono.error(new RuntimeException("Credenciales inválidas")));

        StepVerifier.create(loginUseCase.login(email, password))
                .expectErrorMatches(ex -> ex instanceof RuntimeException
                        && ex.getMessage().equals("Credenciales inválidas"))
                .verify();

        verify(loginGateway).login(email, password);
    }

}
