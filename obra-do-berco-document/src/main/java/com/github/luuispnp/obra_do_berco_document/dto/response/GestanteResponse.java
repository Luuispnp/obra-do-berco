package com.github.luuispnp.obra_do_berco_document.dto.response;

import com.github.luuispnp.obra_do_berco_document.enums.EstadoCivil;

import java.time.LocalDate;
import java.util.UUID;

public record GestanteResponse(

        UUID id,
        String nome,
        String cpf,
        String numeroIdentidade,
        LocalDate dataNascimento,
        EnderecoResponse endereco,
        EstadoCivil estadoCivil,
        String telefone,
        String email

) {
}
