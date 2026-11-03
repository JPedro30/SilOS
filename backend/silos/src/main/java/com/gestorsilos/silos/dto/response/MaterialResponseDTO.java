package com.gestorsilos.silos.dto.response;

import com.gestorsilos.silos.enums.TipoEspecifico;
import com.gestorsilos.silos.enums.TipoGeneral;

public record MaterialResponseDTO(
        Long idMaterial,
        TipoGeneral tipoGeneral,
        TipoEspecifico tipoEspecifico,
        String nombre
) {}
