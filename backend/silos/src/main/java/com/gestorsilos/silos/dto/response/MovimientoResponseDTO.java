package com.gestorsilos.silos.dto.response;

import com.gestorsilos.silos.enums.TipoOperacion;

import java.time.LocalDateTime;

public record MovimientoResponseDTO(
        Long idMovimiento,
        Integer pesoBrutoKg,
        Integer pesoTaraKg,
        Integer pesoNetoKg,
        SiloResponseDTO silo,
        TransportistaResponseDTO transportista,
        EmpresaResponseDTO empresa,
        MaterialResponseDTO material,
        TipoOperacion tipoOperacion,
        LocalDateTime fechaOperacion,
        Float humedadMaterial,
        Float proteinaMaterial,
        Float humedadAmbiente,
        Boolean precipitacion,
        String empresaNombreSnapshot,
        String matriculaSnapshot,
        String transportistaNombreSnapshot
) {}
