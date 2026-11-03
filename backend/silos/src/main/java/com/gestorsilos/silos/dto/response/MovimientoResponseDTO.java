package com.gestorsilos.silos.dto.response;

import com.gestorsilos.silos.entity.Empresa;
import com.gestorsilos.silos.entity.Material;
import com.gestorsilos.silos.entity.Silo;
import com.gestorsilos.silos.entity.Transportista;
import com.gestorsilos.silos.enums.TipoOperacion;

import java.time.LocalDateTime;

public record MovimientoResponseDTO(
        Long idMovimiento,
        Integer pesoBrutoKg,
        Integer pesoTaraKg,
        Integer pesoNetoKg,
        Silo silo,
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
