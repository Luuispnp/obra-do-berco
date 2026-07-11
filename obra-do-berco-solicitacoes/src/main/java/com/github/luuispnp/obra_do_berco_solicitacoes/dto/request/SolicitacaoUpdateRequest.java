package com.github.luuispnp.obra_do_berco_solicitacoes.dto.request;

import com.github.luuispnp.obra_do_berco_solicitacoes.enums.SexoCrianca;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SolicitacaoUpdateRequest(

    @NotNull(message = "A idade gestacional é obrigatória")
    @Min(value = 27, message = "Idade gestacional deve ser maior que 27 semanas")
    @Max(value = 42, message = "Idade gestacional inválida")
    Integer idadeGestacionalSemanas,

    @NotNull(message = "A data provável do parto é obrigatória")
    @Future(message = "A data provável do parto deve ser futura")
    LocalDate dataProvavelParto,

    SexoCrianca sexoCrianca,

    String nomeDoPai,

    Boolean trabalhando,

    String observacaoGravidez,

    @Min(value = 1, message = "Deve haver ao menos 1 pessoa na residência")
    Integer qtdPessoasResidencia,

    @NotNull(message = "É obrigatório informar se possui cartão pré-natal")
    Boolean cartaoPreNatal,

    String religiao,

    String atendimentoSocial,

    String beneficioSocial

) {
}
