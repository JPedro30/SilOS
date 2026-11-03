package com.gestorsilos.silos.dto.response;

public record TransportistaResponseDTO(
        Long idTransportista,
        String matriculaCamion,
        String matriculaRemolque,
        String nombre,
        String dni
) {}
