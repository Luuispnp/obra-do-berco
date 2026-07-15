package github.luuispnp.obra_do_berco_eventos.dto.response;



import github.luuispnp.obra_do_berco_eventos.enums.SexoCrianca;
import github.luuispnp.obra_do_berco_eventos.enums.StatusSolicitacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    String beneficioSocial,
    String motivoRecusa,
    LocalDateTime dataEncerramento

) {

}
