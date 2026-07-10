package com.github.luuispnp.obradoberco.gestante.repository;

import com.github.luuispnp.obradoberco.gestante.entity.Gestante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GestanteRepository extends
        JpaRepository<Gestante, UUID>,
        JpaSpecificationExecutor<Gestante>
{



}
