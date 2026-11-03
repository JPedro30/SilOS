package com.gestorsilos.silos.dto.response;

public record EmpresaResponseDTO(
        Long idEmpresa,
        String cif,
        String nombre,
        String colorHEX
) {}
