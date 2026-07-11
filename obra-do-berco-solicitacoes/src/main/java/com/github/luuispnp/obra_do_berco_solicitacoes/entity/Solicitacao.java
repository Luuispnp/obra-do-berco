package com.github.luuispnp.obra_do_berco_solicitacoes.entity;

import com.github.luuispnp.obra_do_berco_solicitacoes.enums.SexoCrianca;
import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_solicitacao")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "solicitacao_id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private UUID solicitacaoId;

    // Referência ao voluntario-service

    @Column(name = "voluntario_responsavel_id", nullable = false)
    private UUID voluntarioResponsavelId;

    // Referência ao gestante-service

    @Column(name = "gestante_id", nullable = false)
    private UUID gestanteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_solicitacao", nullable = false, length = 20)
    private StatusSolicitacao status;

    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDate dataSolicitacao;

    // Dados da gestação

    @Column(name = "idade_gestacional_semanas", nullable = false)
    private Integer idadeGestacionalSemanas;

    @Column(name = "data_provavel_parto", nullable = false)
    private LocalDate dataProvavelParto;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo_crianca", length = 20)
    private SexoCrianca sexoCrianca;

    @Column(name = "nome_do_pai")
    private String nomeDoPai;

    @Column
    private Boolean trabalhando;

    @Column(name = "observacao_gravidez")
    private String observacaoGravidez;

    @Column(name = "qtd_pessoas_residencia")
    private Integer qtdPessoasResidencia;

    @Column(name = "cartao_pre_natal", nullable = false)
    private Boolean cartaoPreNatal;

    @Column
    private String religiao;

    @Column(name = "atendimento_social")
    private String atendimentoSocial;

    @Column(name = "beneficio_social")
    private String beneficioSocial;

    // Dados da análise

    @Column(name = "motivo_recusa")
    private String motivoRecusa;

    @Column(name = "data_encerramento")
    private LocalDateTime dataEncerramento;

    @PrePersist
    public void PrePersist() {
        this.status = StatusSolicitacao.EM_ANALISE;
        this.dataSolicitacao = LocalDate.now();
    }

}
