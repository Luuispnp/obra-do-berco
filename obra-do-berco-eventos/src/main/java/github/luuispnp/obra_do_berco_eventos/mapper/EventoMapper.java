package github.luuispnp.obra_do_berco_eventos.mapper;

import github.luuispnp.obra_do_berco_eventos.dto.request.EventoRequest;
import github.luuispnp.obra_do_berco_eventos.dto.response.EventoResponse;
import github.luuispnp.obra_do_berco_eventos.entity.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    Evento toEntity(EventoRequest request);

    EventoResponse toResponse(Evento evento);

    List<EventoResponse> toResponseList(List<Evento> eventos);

    void updateEvento(EventoRequest request, @MappingTarget Evento evento);

}
