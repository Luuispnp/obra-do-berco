package com.github.luuispnp.obra_do_berco_solicitacoes.dto.request;

import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import lombok.Data;

@Data
public class StatusUpdateRequest {

    private StatusSolicitacao status;

}
