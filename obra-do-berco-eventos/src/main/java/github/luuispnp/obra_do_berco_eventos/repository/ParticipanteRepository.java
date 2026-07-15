package github.luuispnp.obra_do_berco_eventos.repository;

import github.luuispnp.obra_do_berco_eventos.entity.Participante;
import github.luuispnp.obra_do_berco_eventos.enums.StatusEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipanteRepository extends JpaRepository<Participante, UUID> {

    @Query("SELECT p FROM Participante p WHERE p.evento.status = :status")
    List<Participante> findByEventoStatus(@Param("status") StatusEvento status);

    boolean existsBySolicitacaoIdAndEventoStatus(UUID solicitacaoId, StatusEvento status);

    Optional<Participante> findByParticipanteIdAndEventoEventoId(UUID participanteId, UUID eventoId);

}
