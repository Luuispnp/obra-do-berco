package com.github.luuispnp.obra_do_berco_solicitacoes.service;

import com.github.luuispnp.obra_do_berco_solicitacoes.client.GestanteClient;
import com.github.luuispnp.obra_do_berco_solicitacoes.client.VoluntarioClient;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.AtualizacaoStatusRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoMotivoRecusaRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.request.SolicitacaoUpdateRequest;
import com.github.luuispnp.obra_do_berco_solicitacoes.dto.response.SolicitacaoResponse;
import com.github.luuispnp.obra_do_berco_solicitacoes.entity.Solicitacao;
import com.github.luuispnp.obra_do_berco_solicitacoes.enums.StatusSolicitacao;
import com.github.luuispnp.obra_do_berco_solicitacoes.exception.*;
import com.github.luuispnp.obra_do_berco_solicitacoes.mapper.SolicitacaoMapper;
import com.github.luuispnp.obra_do_berco_solicitacoes.repository.SolicitacaoRepository;
import feign.FeignException;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolicitacaoService {

    private final SolicitacaoMapper mapper;

    private final SolicitacaoRepository solicitacaoRepository;

    private final GestanteClient gestanteClient;

    private final VoluntarioClient voluntarioClient;

    @Transactional
    public SolicitacaoResponse create(SolicitacaoRequest request) {
        validarGestanteExistente(request.gestanteId());
        validarVoluntarioExistente(request.voluntarioResponsavelId());
        Solicitacao solicitacao = mapper.toEntity(request);
        Solicitacao solicitacaoSalva = solicitacaoRepository.save(solicitacao);
        return mapper.toResponse(solicitacaoSalva);
    }

    private void validarGestanteExistente(UUID gestanteId) {
        try {
            gestanteClient.findById(gestanteId);
        } catch (FeignException.NotFound exception) {
            throw new GestanteNaoEncontradaException("Gestante não encontrada.");
        }
    }

    private void validarVoluntarioExistente(UUID voluntarioId) {
        try {
            voluntarioClient.findById(voluntarioId);
        } catch (FeignException.NotFound exception) {
            throw new VoluntarioNaoEncontradoException("Voluntário não encontrado.");
        }
    }

    public List<SolicitacaoResponse> findAllWithFilter(
            StatusSolicitacao status,
            UUID gestanteId,
            LocalDate dataInicio,
            LocalDate dataFim) {
        Specification<Solicitacao> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (gestanteId != null) {
                predicates.add(cb.equal(root.get("gestanteId"), gestanteId));
            }
            if (dataInicio != null && dataFim != null) {
                predicates.add(cb.between(root.get("dataSolicitacao"), dataInicio, dataFim));
            } else if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataSolicitacao"), dataInicio));
            } else if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataSolicitacao"), dataFim));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<Solicitacao> solicitacoes = solicitacaoRepository.findAll(specification);
        return mapper.toResponseList(solicitacoes);
    }

    public SolicitacaoResponse findById(UUID id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada."));
        return mapper.toResponse(solicitacao);
    }

    @Transactional
    public SolicitacaoResponse updateById(
            UUID id,
            @Valid SolicitacaoUpdateRequest request) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada."));
        if (solicitacao.getStatus() == StatusSolicitacao.EM_ANALISE) {
            mapper.update(request, solicitacao);
            Solicitacao solicitacaoAtualizada = solicitacaoRepository.save(solicitacao);
            return mapper.toResponse(solicitacaoAtualizada);
        } else {
            throw new StatusInvalidoParaAlteracaoException();
        }
    }

    @Transactional
    public SolicitacaoResponse aprovarSolicitacao(UUID id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada."));
        if (solicitacao.getStatus() == StatusSolicitacao.ENTREGUE || solicitacao.getStatus() == StatusSolicitacao.APROVADA) {
            throw new StatusInvalidoParaAlteracaoException();
        } else {
            solicitacao.setStatus(StatusSolicitacao.APROVADA);
            Solicitacao solicitacaoAtualizada = solicitacaoRepository.save(solicitacao);
            return mapper.toResponse(solicitacaoAtualizada);
        }
    }

    @Transactional
    public SolicitacaoResponse recusarSolicitacao(
            UUID id,
            @Valid SolicitacaoMotivoRecusaRequest motivoRecusa) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada."));
        if (solicitacao.getStatus() == StatusSolicitacao.ENTREGUE || solicitacao.getStatus() == StatusSolicitacao.APROVADA) {
            throw new StatusInvalidoParaAlteracaoException();
        } else {
            solicitacao.setStatus(StatusSolicitacao.RECUSADA);
            solicitacao.setMotivoRecusa(motivoRecusa.motivoRecusa());
            Solicitacao solicitacaoAtualizada = solicitacaoRepository.save(solicitacao);
            return mapper.toResponse(solicitacaoAtualizada);
        }
    }

    @Transactional
    public SolicitacaoResponse deleteSolicitacao(UUID id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada."));
        if (solicitacao.getStatus() == StatusSolicitacao.EM_ANALISE) {
            solicitacaoRepository.deleteById(id);
            return mapper.toResponse(solicitacao);
        } else {
            throw new StatusInvalidoParaRemocaoException();
        }
    }

    @Transactional
    public void atualizarStatusLote(List<AtualizacaoStatusRequest> atualizacoes) {
        for (AtualizacaoStatusRequest request : atualizacoes) {
            Solicitacao solicitacao = solicitacaoRepository.findById(request.solicitacaoId())
                    .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitacao não encontrada."));
            solicitacao.setStatus(request.status());
            solicitacao.setDataEncerramento(LocalDateTime.now());
        }
    }

}
