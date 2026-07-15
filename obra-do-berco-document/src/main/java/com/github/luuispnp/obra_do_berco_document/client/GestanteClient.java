package com.github.luuispnp.obra_do_berco_document.client;

import com.github.luuispnp.obra_do_berco_document.dto.response.GestanteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "OBRA-DO-BERCO-GESTANTE")
public interface GestanteClient {

    @GetMapping("/gestantes/{id}")
    GestanteResponse findById(@PathVariable("id") UUID id);

}
