package com.github.luuispnp.obra_do_berco_solicitacoes.controller;

import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.StatusUpdateRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.response.SolicitacaoResponse;
import com.github.luuispnp.obra_do_berco_solicitacoes.service.SolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SolicitacaoController {

    @Autowired
    SolicitacaoService solicitacaoService;

    @PostMapping("/solicitacoes")
    public ResponseEntity<SolicitacaoResponse> createSolicitacao(@RequestBody SolicitacaoRequest solicitacaoRequest) {
        return solicitacaoService.createSolicitacao(solicitacaoRequest);
    }

    @GetMapping("/solicitacoes")
    public ResponseEntity<List<SolicitacaoResponse>> getSolicitacoes() {
        return solicitacaoService.getSolicitacoes();
    }

    @GetMapping("/solicitacoes/{solicitacaoId}")
    public ResponseEntity<SolicitacaoResponse> getSolicitacaoById(@PathVariable Long solicitacaoId) {
        return solicitacaoService.getSolicitacaoById(solicitacaoId);
    }

    @PatchMapping("/solicitacoes/{solicitacaoId}/status")
    public ResponseEntity<SolicitacaoResponse> updateStatus(@PathVariable Long solicitacaoId, @RequestBody StatusUpdateRequest statusUpdateRequest) {
        return solicitacaoService.updateStatus(solicitacaoId, statusUpdateRequest);
    }

}
