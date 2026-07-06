package com.github.luuispnp.obra_do_berco_voluntarios.entity;

import com.github.luuispnp.obra_do_berco_voluntarios.enums.PerfilAcesso;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_voluntario")
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "voluntario_id")
    private UUID voluntarioId;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_acesso", nullable = false, length = 30)
    private PerfilAcesso perfil;

}
