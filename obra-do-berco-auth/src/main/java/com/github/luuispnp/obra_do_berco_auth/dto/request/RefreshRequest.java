package com.github.luuispnp.obra_do_berco_auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(

        @NotBlank(message = "O refreshToken é obrigatório")
        String refreshToken

) {
}
