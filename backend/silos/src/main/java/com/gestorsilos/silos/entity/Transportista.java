package com.gestorsilos.silos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transportistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transportista", nullable = false)
    private Long idTransportista;

    @Column(name = "matricula_camion", nullable = false, unique = true, length = 15)
    private String matriculaCamion;

    @Column(name = "matricula_remolque", nullable = false, length = 15)
    private String matriculaRemolque;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "dni", nullable = false, length = 9)
    private String dni;
}
