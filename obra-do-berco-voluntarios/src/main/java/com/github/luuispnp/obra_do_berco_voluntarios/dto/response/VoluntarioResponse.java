package com.github.luuispnp.obra_do_berco_voluntarios.dto.response;

import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import com.github.luuispnp.obra_do_berco_voluntarios.enums.PerfilAcesso;

import java.time.LocalDate;
import java.util.UUID;

public record VoluntarioResponse(

        UUID voluntarioId,
        String nomeCompleto,
        String email,
        String telefone,
        LocalDate dataCadastro,
        PerfilAcesso perfil,
        boolean ativo

) {

}
