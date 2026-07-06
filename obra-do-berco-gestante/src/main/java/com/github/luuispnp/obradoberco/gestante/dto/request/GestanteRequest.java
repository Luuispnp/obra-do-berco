package com.github.luuispnp.obradoberco.gestante.dto.request;

import com.github.luuispnp.obradoberco.gestante.enums.EstadoCivil;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GestanteRequest {

    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private EstadoCivil estadoCivil;
    private String telefoneWhatsapp;
    private String email;
    private EnderecoRequest endereco;

}
