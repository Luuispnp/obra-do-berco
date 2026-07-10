package com.github.luuispnp.obradoberco.gestante.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRequest (

    @NotBlank(message = "CEP é obrigatório.")
    String cep,

    @NotBlank(message = "Logradouro é obrigatório")
    String logradouro,

    @NotBlank(message = "Bairro é obrigatório")
    String bairro,

    @NotBlank(message = "Cidade é obrigatório")
    String cidade,

    String referencia

) {

}
