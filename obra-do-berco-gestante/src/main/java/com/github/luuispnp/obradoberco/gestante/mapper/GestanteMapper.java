package com.github.luuispnp.obradoberco.gestante.mapper;

import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequest;
import com.github.luuispnp.obradoberco.gestante.entity.Gestante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GestanteMapper {

    Gestante gestanteRequestToEntity(GestanteRequest gestanteRequest);

    @Mapping(target = "gestanteId", ignore = true)
    void updateGestanteFromGestanteRequest(
            GestanteRequest gestanteRequest,
            @MappingTarget Gestante gestante
    );

}
