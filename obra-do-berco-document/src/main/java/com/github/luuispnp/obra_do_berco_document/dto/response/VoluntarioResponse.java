package com.github.luuispnp.obra_do_berco_document.dto.response;

import java.util.UUID;

public record VoluntarioResponse(

        UUID voluntarioId,
        String nomeCompleto

) {
}
