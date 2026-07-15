package github.luuispnp.obra_do_berco_eventos.repository;

import github.luuispnp.obra_do_berco_eventos.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventoRepository extends
        JpaRepository<Evento, UUID>,
        JpaSpecificationExecutor<Evento> {
}
