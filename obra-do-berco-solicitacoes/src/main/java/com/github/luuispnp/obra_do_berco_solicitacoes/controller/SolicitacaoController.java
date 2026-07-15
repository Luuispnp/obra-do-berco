package com.github.luuispnp.obra_do_berco_solicitacoes.controller;

import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.AtualizacaoStatusLoteRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoMotivoRecusaRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoUpdateRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.response.SolicitacaoResponse;
import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import com.github.luuispnp.obra_do_berco_solicitacoes.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PostMapping
    public ResponseEntity<SolicitacaoResponse> create(@RequestBody @Valid SolicitacaoRequest request) {
        SolicitacaoResponse response = solicitacaoService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoResponse>> findAllWithFilter(
            @RequestParam(required = false) StatusSolicitacao status,
            @RequestParam(required = false) UUID gestanteId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim
            ) {
        List<SolicitacaoResponse> response = solicitacaoService.findAllWithFilter(status, gestanteId, dataInicio, dataFim);
        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponse> findById(@PathVariable UUID id) {
        SolicitacaoResponse response = solicitacaoService.findById(id);
        return ResponseEntity
                .ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid SolicitacaoUpdateRequest request) {
        SolicitacaoResponse response = solicitacaoService.updateById(id, request);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<SolicitacaoResponse> aprovarSolicitacao(@PathVariable UUID id) {
        SolicitacaoResponse response = solicitacaoService.aprovarSolicitacao(id);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<SolicitacaoResponse> recusarSolicitacao(
            @PathVariable UUID id,
            @RequestBody SolicitacaoMotivoRecusaRequest motivoRecusa) {
        SolicitacaoResponse response = solicitacaoService.recusarSolicitacao(id, motivoRecusa);
        return ResponseEntity
                .ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SolicitacaoResponse> delete(@PathVariable UUID id) {
        SolicitacaoResponse response = solicitacaoService.deleteSolicitacao(id);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/status-lote")
    public ResponseEntity<Void> atualizarStatusLote(@RequestBody AtualizacaoStatusLoteRequest request) {
        solicitacaoService.atualizarStatusLote(request.atualizacoes());
        return ResponseEntity
                .noContent()
                .build();
    }

}
