package com.gestorsilos.silos.entity;

import com.gestorsilos.silos.enums.TipoEspecifico;
import com.gestorsilos.silos.enums.TipoGeneral;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "materiales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Long idMaterial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_general", nullable = false)
    private TipoGeneral tipoGeneral;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_especifico", nullable = false)
    private TipoEspecifico tipoEspecifico;

    @Column(name = "nombre", nullable = false)
    private String nombre;
}
