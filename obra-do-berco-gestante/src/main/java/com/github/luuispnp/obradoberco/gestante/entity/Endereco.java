package com.github.luuispnp.obradoberco.gestante.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Endereco {

    private String logradouro;
    private String bairro;
    private String cidade;
    private String referencia;

}
