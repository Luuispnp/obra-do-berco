package com.github.luuispnp.obra_do_berco_solicitacoes.dto.request;

import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;

import java.util.UUID;

public record AtualizacaoStatusRequest(

        UUID solicitacaoId,
        StatusSolicitacao status

) {
}
