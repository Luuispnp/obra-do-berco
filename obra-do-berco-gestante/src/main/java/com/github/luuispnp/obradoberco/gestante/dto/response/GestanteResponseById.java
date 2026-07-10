package com.github.luuispnp.obradoberco.gestante.dto.response;

import com.github.luuispnp.obradoberco.gestante.entity.Endereco;
import com.github.luuispnp.obradoberco.gestante.enums.EstadoCivil;

import java.time.LocalDate;
import java.util.UUID;

public record GestanteResponseById(

    UUID id,
    String nome,
    String cpf,
    String numeroIdentidade,
    LocalDate dataNascimento,
    Endereco endereco,
    EstadoCivil estadoCivil,
    String telefone,
    String email,
    LocalDate dataCadastro,
    Boolean ativo

) {
}
