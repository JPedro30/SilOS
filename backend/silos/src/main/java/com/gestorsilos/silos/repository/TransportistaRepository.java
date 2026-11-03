package com.gestorsilos.silos.repository;

import com.gestorsilos.silos.entity.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Long> {

    boolean existsByMatriculaCamion(String matriculaCamion);

    Optional<Transportista> findByMatriculaCamion(String matriculaCamion);
}
