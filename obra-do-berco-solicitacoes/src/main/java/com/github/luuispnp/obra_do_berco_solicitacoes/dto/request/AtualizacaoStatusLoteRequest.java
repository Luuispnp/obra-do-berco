package com.github.luuispnp.obra_do_berco_solicitacoes.dto.request;

import java.util.List;

public record AtualizacaoStatusLoteRequest(

        List<AtualizacaoStatusRequest> atualizacoes

) {
}
