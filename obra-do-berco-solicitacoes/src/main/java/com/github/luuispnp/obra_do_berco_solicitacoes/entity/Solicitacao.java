package com.github.luuispnp.obra_do_berco_solicitacoes.entity;

import com.github.luuispnp.obra_do_berco_solicitacoes.enums.SexoCrianca;
import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_solicitacao")
@Data
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitacao_id", nullable = false)
    private Long solicitacaoId;

    @Column(name = "gestante_id", nullable = false)
    private Long gestanteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_solicitacao", nullable = false, length = 20)
    private StatusSolicitacao status;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;

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

    @Column(name = "cartao_pre_natal",nullable = false)
    private boolean cartaoPreNatal;

    @Column
    private String religiao;

    @Column(name = "atendimento_social")
    private String atendimentoSocial;

    @Column(name = "beneficio_social")
    private String beneficioSocial;

}
