package com.gestorsilos.silos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa;

    @Column(name = "cif", nullable = false, unique = true, length = 15)
    private String cif;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "color_hex", nullable = false, length = 7)
    private String colorHEX;
}
