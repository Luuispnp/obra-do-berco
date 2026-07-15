package com.github.luuispnp.obra_do_berco_voluntarios.dto.response;

import com.github.luuispnp.obra_do_berco_voluntarios.enums.PerfilAcesso;

import java.util.UUID;

public record VoluntarioAutenticacaoResponse(

        UUID voluntarioId,
        String nomeCompleto,
        String email,
        PerfilAcesso perfil,
        boolean ativo

) {
}
