package com.github.luuispnp.obra_do_berco_document.dto.response;

import com.github.luuispnp.obra_do_berco_document.enums.SexoCrianca;
import com.github.luuispnp.obra_do_berco_document.enums.StatusSolicitacao;

import java.time.LocalDate;
import java.util.UUID;

public record SolicitacaoResponse(

        UUID solicitacaoId,
        UUID gestanteId,
        UUID voluntarioResponsavelId,
        StatusSolicitacao status,
        LocalDate dataSolicitacao,
        Integer idadeGestacionalSemanas,
        LocalDate dataProvavelParto,
        SexoCrianca sexoCrianca,
        String nomeDoPai,
        Boolean trabalhando,
        String observacaoGravidez,
        Integer qtdPessoasResidencia,
        Boolean cartaoPreNatal,
        String religiao,
        String atendimentoSocial,
        String beneficioSocial

) {
}
