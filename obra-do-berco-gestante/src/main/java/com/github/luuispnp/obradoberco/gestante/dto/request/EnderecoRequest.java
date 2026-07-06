package com.github.luuispnp.obradoberco.gestante.dto.request;

import lombok.Data;

@Data
public class EnderecoRequest {

    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String referencia;

}
