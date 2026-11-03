package com.gestorsilos.silos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "El usuario es obligatorio")
        @Size(min = 4, max = 30, message = "El usuario tiene que tener entre 4 y 30 caracteres")
        String nombre,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
