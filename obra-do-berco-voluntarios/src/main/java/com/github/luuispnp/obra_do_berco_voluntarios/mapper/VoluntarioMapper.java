package com.github.luuispnp.obra_do_berco_voluntarios.mapper;

import com.github.luuispnp.obra_do_berco_voluntarios.dto.request.VoluntarioRequest;
import com.github.luuispnp.obra_do_berco_voluntarios.dto.response.VoluntarioResponse;
import com.github.luuispnp.obra_do_berco_voluntarios.entity.Voluntario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VoluntarioMapper {

    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    Voluntario toEntity(VoluntarioRequest voluntarioRequest);

   VoluntarioResponse toResponse(Voluntario voluntario);

   List<VoluntarioResponse> toResponseList(List<Voluntario> voluntarios);

   void updateVoluntario(VoluntarioRequest voluntarioRequest, @MappingTarget Voluntario voluntario);
}
