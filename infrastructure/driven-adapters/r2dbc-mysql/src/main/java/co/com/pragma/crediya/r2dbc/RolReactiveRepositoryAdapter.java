package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.rol.Rol;
import co.com.pragma.crediya.model.rol.gateways.RolRepository;
import co.com.pragma.crediya.r2dbc.entities.RolEntity;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        Long,
        RolReactiveRepository
> implements RolRepository
{
    private static final Logger log = LoggerFactory.getLogger(RolReactiveRepositoryAdapter.class);

    public RolReactiveRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper, PasswordEncoder passwordEncoder) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Rol.class));
    }

}
