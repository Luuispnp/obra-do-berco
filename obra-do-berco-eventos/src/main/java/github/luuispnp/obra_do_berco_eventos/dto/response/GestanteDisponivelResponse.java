package github.luuispnp.obra_do_berco_eventos.dto.response;

import java.util.UUID;

public record GestanteDisponivelResponse(

        UUID solicitacaoId,
        UUID gestanteId

) {
}
