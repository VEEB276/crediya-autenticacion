package co.com.pragma.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginDTO(
        @Email String correoElectronico,

        @NotBlank String password
) {}
