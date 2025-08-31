package co.com.pragma.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateUserDTO(
        @NotBlank(message = "El nombre es requerido")
        String nombre,

        @NotBlank(message = "El apellido es requerido")
        String apellido,

        String fechaNacimiento,

        String direccion,

        String telefono,

        @Email
        @NotBlank(message = "El correo electrónico es requerido")
        String correoElectronico,

        @NotNull(message = "El salario base es requerido")
        BigDecimal salarioBase,

        String documentoIdentidad,

        Long idRol
) {}
