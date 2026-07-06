package com.github.luuispnp.obra_do_berco_document.dto.response;

import com.github.luuispnp.obra_do_berco_document.enums.StatusSolicitacao;
import lombok.Data;

@Data
public class SolicitacaoResponse {

    private Long id;
    private Long gestanteId;
    private StatusSolicitacao status;
    private Integer idadeGestacionalSemanas;
    private String sexoCrianca;
    private String observacaoGravidez;
    private Boolean trabalhando;
    private Integer qtdPessoasResidencia;
    private String beneficioGoverno;
    private Boolean necessidadeAtendimentoSocial;

}
