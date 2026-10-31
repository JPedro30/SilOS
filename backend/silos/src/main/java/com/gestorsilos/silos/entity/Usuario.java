package com.gestorsilos.silos.entity;

import com.gestorsilos.silos.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Enum<Rol> rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = true)
    private Empresa empresa;

    @AssertTrue(message = "Los usuarios con rol CLIENTE deben de tener una empresa asociada.")
    private boolean isEmpresaValidaSegunRol(){
        if (this.rol == Rol.CLIENTE){
            return this.empresa != null; // Si tiene empresa asociada devuelve true, si no devuelve false y corta ejecucion
        }
        return true; // para ADMIN y TRABAJADORES devuelve siempre true
    }
}
