package com.gestorsilos.silos.dto.request;

import com.gestorsilos.silos.enums.TipoEspecifico;
import com.gestorsilos.silos.enums.TipoGeneral;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MaterialRequestDTO(
        @NotNull(message = "El tipo general es obligatorio")
        TipoGeneral tipoGeneral,

        @NotNull(message = "El tipo especifico es obligatorio")
        TipoEspecifico tipoEspecifico,

        @NotBlank(message = "El nombre comercial del material no peude estar vacio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre
) {}
