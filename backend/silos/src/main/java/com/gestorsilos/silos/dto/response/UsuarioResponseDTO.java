package com.gestorsilos.silos.dto.response;

import com.gestorsilos.silos.entity.Empresa;
import com.gestorsilos.silos.enums.Rol;

public record UsuarioResponseDTO(
        Long idUsuario,
        String nombre,
        String password,
        Rol rol,
        Empresa empresa
) {}
