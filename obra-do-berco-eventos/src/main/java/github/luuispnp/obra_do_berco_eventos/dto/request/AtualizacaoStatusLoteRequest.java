package github.luuispnp.obra_do_berco_eventos.dto.request;

import java.util.List;

public record AtualizacaoStatusLoteRequest(

        List<AtualizacaoStatusRequest> atualizacoes

) {
}
