package com.github.luuispnp.obra_do_berco_solicitacoes.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record SolicitacaoMotivoRecusaRequest(

        @NotBlank(message = "Motivo da recusa é obrigatório.")
        @Max(value = 50, message = "Máximo de 50 caracteres")
        String motivoRecusa

) {
}
