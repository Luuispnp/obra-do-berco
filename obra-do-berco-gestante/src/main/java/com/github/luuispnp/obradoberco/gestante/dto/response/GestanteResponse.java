package com.github.luuispnp.obradoberco.gestante.dto.response;

import com.github.luuispnp.obradoberco.gestante.entity.Gestante;
import lombok.Data;

@Data
public class GestanteResponse {

    private Long id;
    private String nomeCompleto;

    public GestanteResponse(Gestante gestante) {
        this.id = gestante.getGestanteId();
        this.nomeCompleto = gestante.getNomeCompleto();
    }

}
