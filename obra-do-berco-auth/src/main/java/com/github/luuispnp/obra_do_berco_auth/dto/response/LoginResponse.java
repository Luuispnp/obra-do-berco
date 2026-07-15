package com.github.luuispnp.obra_do_berco_auth.dto.response;

public record LoginResponse(

        String accessToken,
        String refreshToken,
        long expiresIn,
        UsuarioResponse user

) {
}
