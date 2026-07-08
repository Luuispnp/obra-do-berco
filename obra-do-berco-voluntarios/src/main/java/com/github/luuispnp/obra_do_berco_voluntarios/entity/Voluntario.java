package com.github.luuispnp.obra_do_berco_voluntarios.entity;

import com.github.luuispnp.obra_do_berco_voluntarios.enums.PerfilAcesso;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "senha")
@Entity
@Table(name = "tb_voluntario")
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "voluntario_id")
    @EqualsAndHashCode.Include
    private UUID voluntarioId;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_acesso", nullable = false, length = 30)
    private PerfilAcesso perfil;

    @Column(nullable = false)
    private boolean ativo;

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();
        }
        ativo = true;
        perfil = PerfilAcesso.VOLUNTARIO;
    }

}
