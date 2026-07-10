package com.github.luuispnp.obradoberco.gestante.entity;

import com.github.luuispnp.obradoberco.gestante.enums.EstadoCivil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_gestante")
public class Gestante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "numero_identidade", unique = true, length = 25)
    private String numeroIdentidade;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil", nullable = false, length = 20)
    private EstadoCivil estadoCivil;

    @Column(nullable = false)
    private String telefone;

    @Column(unique = true)
    private String email;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null)
            dataCadastro = LocalDate.now();

        ativo = true;
    }

}
