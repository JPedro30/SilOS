package com.gestorsilos.silos.dto.request;

import com.gestorsilos.silos.enums.Estado;
import jakarta.validation.constraints.*;

public record SiloRequestDTO(

        @NotNull(message = "El ID de la empresa es obligatorio")
        Long idEmpresa,

        Long idMaterial,

        @NotBlank(message = "El nombre del silo es obligatorio")
        @Size(min = 2, max = 8, message = "El nombre del silo debe contener entre 2 y 8 caracteres")
        String nombreSilo,

        @NotNull(message = "La capacidad máxima es obligatoria")
        @Positive(message = "La capacidad máxima debe ser mayor que 0")
        @Digits(integer = 6, fraction = 0, message = "La capacidad máxima no puede superar los 999.999 kg")
        Integer capacidadMax,

        @NotNull(message = "El estado es obligatorio")
        Estado estado
) {}
