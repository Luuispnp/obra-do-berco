package com.github.luuispnp.obradoberco.gestante.mapper;

import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequest;
import com.github.luuispnp.obradoberco.gestante.dto.request.GestanteRequestUpdate;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponse;
import com.github.luuispnp.obradoberco.gestante.dto.response.GestanteResponseById;
import com.github.luuispnp.obradoberco.gestante.entity.Gestante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GestanteMapper {

    Gestante toEntity(GestanteRequest request);

    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "mascararCpf")
    GestanteResponse toResponse(Gestante gestante);

    GestanteResponseById toResponseById(Gestante gestante);

    void updateGestante(GestanteRequestUpdate request, @MappingTarget Gestante gestante);

    List<GestanteResponse> toResponseList(List<Gestante> gestantes);

    @Named("mascararCpf")
    default String mascararCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) return cpf;

        String cpfLimpo = cpf.replaceAll("\\D", "");

        if (cpfLimpo.length() != 11) return cpf;

        return cpfLimpo.substring(0, 3) + ".***.***-" + cpfLimpo.substring(9, 11);
    }

}
