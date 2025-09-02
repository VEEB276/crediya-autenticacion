package co.com.pragma.crediya.api.dto;

import java.math.BigDecimal;

public record ResponseUserDTO(
        Long idUsuario,
        String nombre,
        String apellido,
        String fechaNacimiento,
        String direccion,
        String telefono,
        String correoElectronico,
        BigDecimal salarioBase,
        String documentoIdentidad,
        String password,
        Long idRol
) {}
