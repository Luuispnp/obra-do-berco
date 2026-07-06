package com.github.luuispnp.obra_do_berco_solicitacoes.dto.response;

import com.github.luuispnp.obra_do_berco_solicitacoes.entity.Solicitacao;
import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SolicitacaoResponse {

    private Long id;
    private StatusSolicitacao status;
    private GestanteResponse gestante;

    public SolicitacaoResponse(Solicitacao solicitacao, GestanteResponse gestante) {
        this.id = solicitacao.getSolicitacaoId();
        this.status = solicitacao.getStatus();
        this.gestante = gestante;
    }
}
