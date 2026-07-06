package com.github.luuispnp.obra_do_berco_solicitacoes.dto.request;

import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SolicitacaoRequest {

    private Long id;
    private Long gestanteId;
    private LocalDate dataSolicitacao;
    private StatusSolicitacao status;
    private Integer idadeGestacionalSemanas;
    private LocalDate dataProvavelParto;
    private String sexoCrianca;
    private Boolean trabalhando;
    private String observacaoGravidez;
    private Integer qtdPessoasResidencia;
    private String precisaAtendimentoSocial;
    private String recebeBeneficioGoverno;

}
