package com.github.luuispnp.obradoberco.gestante.dto.request;

import com.github.luuispnp.obradoberco.gestante.enums.EstadoCivil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GestanteRequestUpdate(

    @NotBlank(message = "Nome Completo é obrigatório.")
    String nome,

    @NotNull(message = "Estado civil é obrigatório.")
    EstadoCivil estadoCivil,

    @NotNull(message = "Telefone é obrigatório.")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    String telefone,

    @Email(message = "Formato de e-mail inválido")
    @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres.")
    String email,

    @NotNull(message = "Endereço é obrigatório.")
    EnderecoRequest endereco

) {

}
