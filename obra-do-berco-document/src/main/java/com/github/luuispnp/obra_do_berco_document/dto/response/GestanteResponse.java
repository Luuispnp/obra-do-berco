package com.github.luuispnp.obra_do_berco_document.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GestanteResponse {

    private Long id;
    private String nomeCompleto;
    private String identidade;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estadoCivil;
    private String religiao;
    private String email;
    private String nomePai;

}