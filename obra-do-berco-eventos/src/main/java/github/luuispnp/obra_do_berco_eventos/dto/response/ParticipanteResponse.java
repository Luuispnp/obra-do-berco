package github.luuispnp.obra_do_berco_eventos.dto.response;

import java.util.UUID;

public record ParticipanteResponse(

        UUID participanteId,
        UUID solicitacaoId,
        UUID gestanteId,
        String nomeGestante,
        Boolean presente

) {
}
