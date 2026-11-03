package com.gestorsilos.silos.dto.response;

import com.gestorsilos.silos.enums.Estado;

public record SiloResponseDTO(
        Long idSilo,
        EmpresaResponseDTO empresa,
        MaterialResponseDTO material,
        String nombreSilo,
        Integer capacidadMax,
        Integer capacidadActual,
        Estado estado
) {}
