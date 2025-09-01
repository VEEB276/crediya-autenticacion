package co.com.pragma.crediya.api.mapper;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.model.usuario.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    default Usuario toModel(CreateUserDTO dto) {
        if (dto == null) return null;
        return new Usuario(
                null,
                dto.nombre(),
                dto.apellido(),
                dto.fechaNacimiento(),
                dto.direccion(),
                dto.telefono(),
                dto.correoElectronico(),
                dto.salarioBase(),
                dto.documentoIdentidad(),
                dto.idRol()
        );
    }

    ResponseUserDTO toResponse(Usuario solicitud);

}
