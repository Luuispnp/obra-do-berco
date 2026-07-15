package github.luuispnp.obra_do_berco_eventos.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AdicionarParticipanteRequest(

        @NotNull(message = "O ID da solicitação é obrigatório.")
        UUID solicitacaoId

) {
}
