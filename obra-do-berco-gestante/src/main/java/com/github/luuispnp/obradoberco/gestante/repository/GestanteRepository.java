package com.github.luuispnp.obradoberco.gestante.repository;

import com.github.luuispnp.obradoberco.gestante.entity.Gestante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GestanteRepository extends JpaRepository<Gestante, Long> {

    boolean existsByCpf(String cpf);

}
