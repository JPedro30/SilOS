package com.gestorsilos.silos.repository;

import com.gestorsilos.silos.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByCif(String cif);

    Optional<Empresa> findByCif(String cif);

}
