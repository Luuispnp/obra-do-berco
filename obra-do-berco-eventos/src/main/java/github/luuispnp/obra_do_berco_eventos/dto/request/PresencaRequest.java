package github.luuispnp.obra_do_berco_eventos.dto.request;

import jakarta.validation.constraints.NotNull;

public record PresencaRequest(

        @NotNull(message = "O campo presente é obrigatório.")
        Boolean presente

) {
}
