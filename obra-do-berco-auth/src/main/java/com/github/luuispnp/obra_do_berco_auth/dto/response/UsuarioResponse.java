package com.github.luuispnp.obra_do_berco_auth.dto.response;

import java.util.UUID;

public record UsuarioResponse(

        UUID id,
        String nome,
        String email,
        String role

) {
}
