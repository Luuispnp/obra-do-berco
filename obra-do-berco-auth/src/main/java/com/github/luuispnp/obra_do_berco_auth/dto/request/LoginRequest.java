package com.github.luuispnp.obra_do_berco_auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "E-mail é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha

) {
}
