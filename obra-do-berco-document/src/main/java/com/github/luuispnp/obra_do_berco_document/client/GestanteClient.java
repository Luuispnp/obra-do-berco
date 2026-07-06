package com.github.luuispnp.obra_do_berco_document.client;

import com.github.luuispnp.obra_do_berco_document.dto.response.GestanteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("OBRA-DO-BERCO-GESTANTE")
public interface GestanteClient {

    @GetMapping("/api/gestantes/{gestanteId}")
    ResponseEntity<GestanteResponse> getGestanteById(@PathVariable Long gestanteId);

}
