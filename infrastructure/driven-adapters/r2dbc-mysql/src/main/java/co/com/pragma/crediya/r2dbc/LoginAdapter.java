package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.exception.BusinessException;
import co.com.pragma.crediya.exception.ValidationException;
import co.com.pragma.crediya.model.usuario.login.gateways.LoginGateway;
import co.com.pragma.crediya.r2dbc.security.jwt.provider.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LoginAdapter implements LoginGateway
{
    private static final Logger log = LoggerFactory.getLogger(LoginAdapter.class);

    private final MyReactiveRepository myReactiveRepository;
    private final RolReactiveRepository rolReactiveRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginAdapter(MyReactiveRepository myReactiveRepository, RolReactiveRepository rolReactiveRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.myReactiveRepository = myReactiveRepository;
        this.rolReactiveRepository = rolReactiveRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<String> login(String email, String password) {
        log.info("Intento de login para email: {}", email);
        log.info("Password ingresada (raw): {}", password);

        return myReactiveRepository.findByCorreoElectronico(email)
                .switchIfEmpty(Mono.error(new ValidationException("Usuario no encontrado")))
                .filter(userDocument -> {
                    boolean matches = passwordEncoder.matches(password, userDocument.getPassword());
                    log.info("Coincide contraseña? {}", matches);
                    return matches;
                })
                .flatMap(userDocument ->
                        rolReactiveRepository.findById(userDocument.getIdRol())
                                .map(rol -> {
                                    log.info("Rol del usuario: {}", rol.getNombre());
                                    return jwtProvider.generateToken(userDocument.getCorreoElectronico(), rol.getNombre());
                                })
                )
                .switchIfEmpty(Mono.error(new BusinessException("bad credentials")));
    }

}
