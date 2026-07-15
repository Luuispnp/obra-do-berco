package github.luuispnp.obra_do_berco_eventos.dto.request;

import java.util.UUID;

public record AtualizacaoStatusRequest(

        UUID solicitacaoId,
        String status

) {
}
