package com.gestorsilos.silos.repository;

import com.gestorsilos.silos.entity.*;
import com.gestorsilos.silos.enums.TipoOperacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    Page<Movimiento> findBySilo_IdSilo(Long idSilo, Pageable pageable);

    Page<Movimiento> findByTransportista_IdTransportista(Long idTransportista, Pageable pageable);

    Page<Movimiento> findByEmpresa_IdEmpresa(Long idEmpresa, Pageable pageable);

    Page<Movimiento> findByMaterial_IdMaterial(Long idMaterial, Pageable pageable);

    Page<Movimiento> findByTipoOperacionAndSilo_IdSilo(TipoOperacion tipoOperacion, Long siloIdSilo, Pageable pageable);

    Page<Movimiento> findByTipoOperacionAndSilo_IdSiloAndFechaOperacionBetween(TipoOperacion tipoOperacion, Long siloIdSilo, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    Optional<Movimiento> findFirstBySilo_IdSiloOrderByFechaOperacionDesc(Long siloIdSilo);



}
