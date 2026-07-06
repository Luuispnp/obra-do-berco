package com.github.luuispnp.obra_do_berco_voluntarios.dto.response;

import com.github.luuispnp.obra_do_berco_voluntarios.enums.PerfilAcesso;

import java.util.UUID;

public record VoluntarioResponse(

        UUID voluntarioId,
        String nomeCompleto,
        PerfilAcesso perfil

) {}
