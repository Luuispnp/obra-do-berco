package com.github.luuispnp.obradoberco.gestante.entity;

import com.github.luuispnp.obradoberco.gestante.enums.EstadoCivil;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "tb_gestante")
@Data
public class Gestante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gestante_id", nullable = false)
    private Long gestanteId;

    @Column(name = "nome_completo",nullable = false, length = 150)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "numero_identidade",nullable = false, unique = true, length = 25)
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

}
