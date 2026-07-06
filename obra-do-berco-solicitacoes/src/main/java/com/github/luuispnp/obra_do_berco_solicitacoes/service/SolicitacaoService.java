package com.github.luuispnp.obra_do_berco_solicitacoes.service;

import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.StatusUpdateRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.response.GestanteResponse;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.response.SolicitacaoResponse;
import com.github.luuispnp.obra_do_berco_solicitacoes.entity.Solicitacao;
import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import com.github.luuispnp.obra_do_berco_solicitacoes.exception.SolicitacaoNaoEncontradaException;
import com.github.luuispnp.obra_do_berco_solicitacoes.client.GestanteClient;
import com.github.luuispnp.obra_do_berco_solicitacoes.mapper.SolicitacaoMapper;
import com.github.luuispnp.obra_do_berco_solicitacoes.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitacaoService {

    @Autowired
    GestanteClient gestanteClient;

    @Autowired
    SolicitacaoMapper solicitacaoMapper;

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    public ResponseEntity<SolicitacaoResponse> createSolicitacao(SolicitacaoRequest solicitacaoRequest) {
        GestanteResponse gestante = gestanteClient.getGestanteById(solicitacaoRequest.getGestanteId()).getBody();
        Solicitacao solicitacao = solicitacaoMapper.solicitacaoRequestToEntity(solicitacaoRequest);
        solicitacao.setStatus(StatusSolicitacao.EM_ANALISE);
        solicitacao.setDataSolicitacao(LocalDate.now());
        Solicitacao solicitacaoSalva = solicitacaoRepository.save(solicitacao);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SolicitacaoResponse(solicitacaoSalva, gestante));
    }

    public ResponseEntity<List<SolicitacaoResponse>> getSolicitacoes() {
        List<Solicitacao> solicitacoes = solicitacaoRepository.findAll();
        List<SolicitacaoResponse> responses = solicitacoes.stream()
                .map(solicitacao -> {
                    GestanteResponse gestante =
                            gestanteClient.getGestanteById(solicitacao.getGestanteId()).getBody();
                    return new SolicitacaoResponse(solicitacao, gestante);
                })
                .toList();
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<SolicitacaoResponse> getSolicitacaoById(Long solicitacaoId) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada"));
        GestanteResponse gestante = gestanteClient.getGestanteById(solicitacao.getGestanteId()).getBody();
        return ResponseEntity.ok(new SolicitacaoResponse(solicitacao, gestante));
    }

    @Transactional
    public ResponseEntity<SolicitacaoResponse> updateStatus(Long solicitacaoId, StatusUpdateRequest statusUpdateRequest) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada"));
        solicitacao.setStatus(statusUpdateRequest.getStatus());
        GestanteResponse gestante = gestanteClient.getGestanteById(solicitacao.getGestanteId()).getBody();
        SolicitacaoResponse solicitacaoResponse = new SolicitacaoResponse(solicitacao, gestante);
        solicitacaoRepository.save(solicitacao);
        return ResponseEntity.ok(solicitacaoResponse);
    }
}
