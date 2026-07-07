package com.github.luuispnp.obra_do_berco_voluntarios.repository;

import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, UUID> {

    Optional<Voluntario> findByEmail(String email);

    Optional<Voluntario> findById(UUID voluntarioId);

    List<Voluntario> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);
}
