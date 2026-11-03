package com.gestorsilos.silos.repository;

import com.gestorsilos.silos.entity.Silo;
import com.gestorsilos.silos.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiloRepository extends JpaRepository<Silo, Long> {

    List<Silo> findByEmpresa_IdEmpresa(Long idEmpresa);

    List<Silo> findByMaterial_IdMaterial(Long idMaterial);

    List<Silo> findByEstado(Estado estado);

    boolean existsByNombreSilo(String nombreSilo);

}
