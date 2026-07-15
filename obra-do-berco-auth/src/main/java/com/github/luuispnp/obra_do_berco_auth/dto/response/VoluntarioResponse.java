package com.github.luuispnp.obra_do_berco_auth.dto.response;

import java.util.UUID;

public record VoluntarioResponse(

        UUID voluntarioId,
        String nomeCompleto,
        String email,
        String perfil

) {
}
