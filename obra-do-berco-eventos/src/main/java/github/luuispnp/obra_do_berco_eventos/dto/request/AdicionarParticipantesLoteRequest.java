package github.luuispnp.obra_do_berco_eventos.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record AdicionarParticipantesLoteRequest(

        @NotEmpty(message = "A lista de solicitações é obrigatória.")
        List<UUID> solicitacaoIds

) {
}
