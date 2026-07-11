package com.github.luuispnp.obra_do_berco_solicitacoes.client;

import com.github.luuispnp.obra_do_berco_solicitacoes.dto.response.GestanteResponseById;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "OBRA-DO-BERCO-GESTANTE")
public interface GestanteClient {

    @GetMapping("/gestantes/{id}")
    GestanteResponseById findById(@PathVariable("id") UUID id);

}
