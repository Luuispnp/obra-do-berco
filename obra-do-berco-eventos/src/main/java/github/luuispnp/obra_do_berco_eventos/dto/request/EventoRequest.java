package github.luuispnp.obra_do_berco_eventos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EventoRequest(

        @NotBlank(message = "O título é obrigatório.")
        String titulo,

        @NotNull(message = "A data é obrigatória.")
        LocalDate dataEvento

) {
}
