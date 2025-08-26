package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.exception.UserNotFoundException;
import co.com.pragma.crediya.model.usuario.Usuario;
import co.com.pragma.crediya.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.crediya.r2dbc.entities.UsuarioEntity;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        Long,
        MyReactiveRepository
> implements UsuarioRepository
{
    private static final Logger log = LoggerFactory.getLogger(MyReactiveRepositoryAdapter.class);

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    public Mono<Usuario> saveUser(Usuario user) {
        return super.save(user)
                .doOnError(error -> log.error("Error guardando usuario: {}", error.getMessage(), error));
    }

    @Override
    public Mono<Usuario> findByCorreo(String correo) {
        return repository.findByCorreoElectronico(correo);
    }

    @Override
    public Flux<Usuario> getAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<Usuario> getUserById(Long id) {
        return super.findById(id);
    }

    @Override
    public Mono<Usuario> editUser(Usuario user) {
        return super.findById(user.getId())
                .flatMap(existingUser -> super.save(user))
                .switchIfEmpty(Mono.error(new UserNotFoundException()));
    }


    @Override
    public Mono<Void> deleteUser(Long idNumber) {
        return repository.deleteById(idNumber);
    }

}
