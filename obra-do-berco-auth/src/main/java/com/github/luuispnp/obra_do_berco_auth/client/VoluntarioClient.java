package com.github.luuispnp.obra_do_berco_auth.client;

import com.github.luuispnp.obra_do_berco_auth.dto.request.VoluntarioAutenticacaoRequest;
import com.github.luuispnp.obra_do_berco_auth.dto.response.VoluntarioAutenticacaoResponse;
import com.github.luuispnp.obra_do_berco_auth.dto.response.VoluntarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "OBRA-DO-BERCO-VOLUNTARIOS")
public interface VoluntarioClient {

    @PostMapping("/voluntarios/autenticar")
    VoluntarioAutenticacaoResponse autenticar(@RequestBody VoluntarioAutenticacaoRequest request);

    @GetMapping("/voluntarios/{id}")
    VoluntarioResponse findById(@PathVariable("id") UUID id);

}
