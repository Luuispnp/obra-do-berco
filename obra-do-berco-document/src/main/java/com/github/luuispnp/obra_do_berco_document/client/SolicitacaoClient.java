package com.github.luuispnp.obra_do_berco_document.client;

import com.github.luuispnp.obra_do_berco_document.dto.response.SolicitacaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("OBRA-DO-BERCO-SOLICITACOES")
public interface SolicitacaoClient {

    @GetMapping("/api/solicitacoes/{solicitacaoId}")
    ResponseEntity<SolicitacaoResponse> getSolicitacaoById(@PathVariable Long solicitacaoId);

}
