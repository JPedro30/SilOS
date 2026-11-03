package com.gestorsilos.silos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TransportistaRequestDTO(
        @NotBlank(message = "La matricula del camion es obligatoria")
        @Pattern(regexp = "^[0-9]{4}[A-Z]{3}$", message = "Formato de matrícula español no válido (ejemplo: 1234ABC)")
        String matriculaCamion,

        @NotBlank(message = "La matricula del remolque es obligatoria")
        String matriculaRemolque,

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^[ABCDEFGHJNPQRSUVW0-9][0-9]{7}[A-Z0-9]$", message = "Formato de CIF/DNI no válido")
        String dni
) {}
