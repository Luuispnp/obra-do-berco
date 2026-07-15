package com.github.luuispnp.obra_do_berco_auth.dto.response;

public record RefreshResponse(

        String accessToken,
        long expiresIn

) {
}
