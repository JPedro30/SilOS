package com.gestorsilos.silos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpresaRequestDTO(
        @NotBlank(message = "El nombre de la empresa es obligatorio")
        @Size(min = 4, max = 30, message = "El nombre debe de contener entre 4 y 30 caracteres")
        String nombre,

        @NotBlank(message = "El CIF de la empresa debe de ser obligatorio")
        @Pattern(regexp = "^[ABCDEFGHJNPQRSUVW0-9][0-9]{7}[A-Z0-9]$", message = "Formato de CIF/DNI no válido")
        String cif,

        @NotBlank(message = "El codigo HEX es obligatorio")
        @Pattern(regexp = "^#([A-Fa-fac0-9]{6}|[A-Fa-fac0-9]{3})$", message = "El color debe ser un código HEX válido (ejemplo: #FF5733)")
        String colorHEX
) {}
