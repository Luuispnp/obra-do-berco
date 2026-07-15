package github.luuispnp.obra_do_berco_eventos.dto.response;

import github.luuispnp.obra_do_berco_eventos.entity.Participante;
import github.luuispnp.obra_do_berco_eventos.enums.StatusEvento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventoResponse(

        UUID eventoId,
        String titulo,
        LocalDate dataEvento,
        StatusEvento status,
        LocalDateTime dataCriacao,
        LocalDateTime dataFinalizacao,
        List<Participante> participantes

) {
}
