package com.github.luuispnp.obra_do_berco_document.dto.response;

import com.github.luuispnp.obra_do_berco_document.enums.StatusEvento;

import java.time.LocalDate;
import java.util.UUID;

public record EventoResponse(

        UUID eventoId,
        String titulo,
        LocalDate dataEvento,
        StatusEvento status

) {
}
