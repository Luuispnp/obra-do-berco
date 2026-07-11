package com.github.luuispnp.obra_do_berco_solicitacoes.dto.response;

import com.github.luuispnp.obra_do_berco_solicitacoes.enums.EstadoCivil;

import java.time.LocalDate;
import java.util.UUID;

public record GestanteResponseById(

    UUID id,
    String nome,
    String cpf,
    String numeroIdentidade,
    LocalDate dataNascimento,
    EnderecoResponse endereco,
    EstadoCivil estadoCivil,
    String telefone,
    String email,
    LocalDate dataCadastro,
    Boolean ativo

) {
}
