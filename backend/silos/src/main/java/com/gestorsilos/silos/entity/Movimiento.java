package com.gestorsilos.silos.entity;

import com.gestorsilos.silos.enums.TipoOperacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table( name = "movimientos",
        indexes = { @Index(name = "idx_movimiento_fecha", columnList = "fecha_operacion"),
                    @Index(name = "idx_movimiento_silo", columnList = "id_silo"),
                    @Index(name = "idx_movimiento_empresa", columnList = "id_empresa"),
                    @Index(name = "idx_movimiento_tipo", columnList = "tipo_operacion") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento", nullable = false)
    private Long idMovimiento;

    @Column(name = "peso_bruto_kg", nullable = false)
    private Integer pesoBrutoKg;

    @Column(name = "peso_tara_kg", nullable = false)
    private Integer pesoTaraKg;

    @Column(name = "peso_neto_kg", nullable = false)
    private Integer pesoNetoKg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_silo", nullable = false)
    private Silo silo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_transportista", nullable = false)
    private Transportista transportista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_material", nullable = false)
    private Material material;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacion", nullable = false, length = 10)
    private TipoOperacion tipoOperacion;

    @PrePersist
    protected void onCreate(){
        if(this.fechaOperacion == null){
            this.fechaOperacion = LocalDateTime.now();
        }
    }

    @Column(name = "fecha_operacion", nullable = false)
    private LocalDateTime fechaOperacion;

    @Column(name = "humedad_material", nullable = true)
    private Float humedadMaterial;

    @Column(name = "proteina_material", nullable = true)
    private Float proteinaMaterial;

    @Column(name = "humedad_ambiente", nullable = true)
    private Float humedadAmbiente;

    @Column(name = "precipitacion", nullable = true)
    private Boolean precipitacion;

    @Column(name = "empresa_nombre_snapshot", nullable = false)
    private String empresaNombreSnapshot;

    @Column(name = "matricula_snapshot", nullable = false)
    private String matriculaSnapshot;

    @Column(name = "transportista_nombre_snapshot", nullable = false)
    private String transportistaNombreSnapshot;

}
