package github.luuispnp.obra_do_berco_eventos.dto.response;

import github.luuispnp.obra_do_berco_eventos.enums.StatusSolicitacao;

import java.util.UUID;

public record SolicitacaoResumoResponse(

        UUID solicitacaoId,
        UUID gestanteId,
        StatusSolicitacao status

) {
}
