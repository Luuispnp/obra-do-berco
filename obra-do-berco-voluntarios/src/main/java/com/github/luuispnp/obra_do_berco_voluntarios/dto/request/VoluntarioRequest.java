package com.github.luuispnp.obra_do_berco_voluntarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VoluntarioRequest(

        @NotBlank(message = "Nome é obrigatório.")
        String nomeCompleto,

        @Email(message = "Email inválido.")
        @NotBlank(message = "Email é obrigatório.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 20,
                message = "A senha deve estar entre {min} e {max} caracteres.")
        String senha,

        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(
                regexp = "^\\d{11}$",
                message = "O telefone deve conter exatamente 11 dígitos."
        )
        String telefone

) {}
