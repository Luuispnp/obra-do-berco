package com.github.luuispnp.obra_do_berco_document.client;

import com.github.luuispnp.obra_do_berco_document.dto.response.EventoResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.ParticipanteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "OBRA-DO-BERCO-EVENTOS")
public interface EventoClient {

    @GetMapping("/eventos/{id}")
    EventoResponse findById(@PathVariable("id") UUID id);

    @GetMapping("/eventos/{eventoId}/participantes")
    List<ParticipanteResponse> findParticipantes(@PathVariable("eventoId") UUID eventoId);

}
