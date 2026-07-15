package github.luuispnp.obra_do_berco_eventos.service;

import github.luuispnp.obra_do_berco_eventos.client.GestanteClient;
import github.luuispnp.obra_do_berco_eventos.client.SolicitacaoClient;
import github.luuispnp.obra_do_berco_eventos.dto.request.AdicionarParticipanteRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.AdicionarParticipantesLoteRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.AtualizacaoStatusLoteRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.AtualizacaoStatusRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.EventoRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.PresencaRequest;
import github.luuispnp.obra_do_berco_eventos.dto.response.EventoResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.GestanteDisponivelResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.ParticipanteResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.SolicitacaoResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.SolicitacaoResumoResponse;
import github.luuispnp.obra_do_berco_eventos.entity.Evento;
import github.luuispnp.obra_do_berco_eventos.entity.Participante;
import github.luuispnp.obra_do_berco_eventos.enums.StatusEvento;
import github.luuispnp.obra_do_berco_eventos.enums.StatusSolicitacao;
import github.luuispnp.obra_do_berco_eventos.exception.EventoNaoEncontradoException;
import github.luuispnp.obra_do_berco_eventos.exception.ParticipanteNaoEncontradoException;
import github.luuispnp.obra_do_berco_eventos.exception.SolicitacaoJaVinculadaException;
import github.luuispnp.obra_do_berco_eventos.exception.SolicitacaoNaoAprovadaException;
import github.luuispnp.obra_do_berco_eventos.exception.SolicitacaoNaoEncontradaException;
import github.luuispnp.obra_do_berco_eventos.exception.StatusInvalidoParaAlteracaoException;
import github.luuispnp.obra_do_berco_eventos.exception.StatusInvalidoParaRemocaoException;
import github.luuispnp.obra_do_berco_eventos.mapper.EventoMapper;
import github.luuispnp.obra_do_berco_eventos.repository.EventoRepository;
import github.luuispnp.obra_do_berco_eventos.repository.ParticipanteRepository;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoService {

    private final EventoRepository eventoRepository;

    private final EventoMapper eventoMapper;

    private final SolicitacaoClient solicitacaoClient;

    private final GestanteClient gestanteClient;

    private final ParticipanteRepository participanteRepository;

    @Transactional
    public EventoResponse createEvento(@Valid EventoRequest request) {
        Evento evento = eventoMapper.toEntity(request);
        Evento eventoSalvo = eventoRepository.save(evento);
        return eventoMapper.toResponse(eventoSalvo);
    }

    public List<EventoResponse> getEventosWithFilter(
            LocalDate dataInicio,
            LocalDate dataFim,
            StatusEvento status) {
        Specification<Evento> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (dataInicio != null && dataFim != null) {
                predicates.add(cb.between(root.get("dataEvento"), dataInicio, dataFim));
            } else if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataEvento"), dataInicio));
            } else if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataEvento"), dataFim));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        List<Evento> eventos = eventoRepository.findAll(specification);
        return eventoMapper.toResponseList(eventos);
    }

    public EventoResponse getEventoById(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        return eventoMapper.toResponse(evento);
    }

    @Transactional
    public EventoResponse updateById(UUID eventoId, @Valid EventoRequest request) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        if (evento.getStatus() == StatusEvento.CRIADO) {
            eventoMapper.updateEvento(request, evento);
            return eventoMapper.toResponse(evento);
        } else {
            throw new StatusInvalidoParaAlteracaoException();
        }
    }

    @Transactional
    public EventoResponse deleteById(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        if (evento.getStatus() == StatusEvento.CRIADO) {
            eventoRepository.deleteById(eventoId);
            return eventoMapper.toResponse(evento);
        } else {
            throw new StatusInvalidoParaRemocaoException();
        }
    }

    @Transactional
    public void finalizarEvento(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        List<AtualizacaoStatusRequest> atualizacoes = evento.getParticipantes().stream()
                .map(this::montarAtualizacao)
                .toList();
        solicitacaoClient.atualizarStatusLote(new AtualizacaoStatusLoteRequest(atualizacoes));
        evento.setStatus(StatusEvento.FINALIZADO);
        evento.setDataFinalizacao(LocalDateTime.now());
        eventoRepository.save(evento);
    }

    private AtualizacaoStatusRequest montarAtualizacao(Participante participante) {
        String status = Boolean.TRUE.equals(participante.getPresente()) ? "ENTREGUE" : "NAO_COMPARECEU";
        return new AtualizacaoStatusRequest(participante.getSolicitacaoId(), status);
    }


    public List<GestanteDisponivelResponse> getGestantesDisponiveis() {
        List<SolicitacaoResumoResponse> aprovadas = solicitacaoClient.findAllWithFilter(StatusSolicitacao.APROVADA);
        Set<UUID> jaVinculadasEmEventoAberto = participanteRepository
                .findByEventoStatus(StatusEvento.CRIADO)
                .stream()
                .map(Participante::getSolicitacaoId)
                .collect(Collectors.toSet());
        return aprovadas.stream()
                .filter(s -> !jaVinculadasEmEventoAberto.contains(s.solicitacaoId()))
                .map(s -> new GestanteDisponivelResponse(s.solicitacaoId(), s.gestanteId()))
                .toList();
    }

    public List<ParticipanteResponse> getParticipantes(UUID eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        return evento.getParticipantes().stream()
                .map(this::toParticipanteResponse)
                .toList();
    }

    @Transactional
    public ParticipanteResponse adicionarParticipante(UUID eventoId, @Valid AdicionarParticipanteRequest request) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        Participante participante = vincularSolicitacao(evento, request.solicitacaoId());
        return toParticipanteResponse(participante);
    }

    @Transactional
    public List<ParticipanteResponse> adicionarParticipantesLote(UUID eventoId, @Valid AdicionarParticipantesLoteRequest request) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        return request.solicitacaoIds().stream()
                .map(solicitacaoId -> vincularSolicitacao(evento, solicitacaoId))
                .map(this::toParticipanteResponse)
                .toList();
    }

    @Transactional
    public ParticipanteResponse removerParticipante(UUID eventoId, UUID participanteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        if (evento.getStatus() != StatusEvento.CRIADO) {
            throw new StatusInvalidoParaRemocaoException();
        }
        Participante participante = participanteRepository.findByParticipanteIdAndEventoEventoId(participanteId, eventoId)
                .orElseThrow(ParticipanteNaoEncontradoException::new);
        participanteRepository.deleteById(participanteId);
        return toParticipanteResponse(participante);
    }

    @Transactional
    public ParticipanteResponse atualizarPresenca(UUID eventoId, UUID participanteId, @Valid PresencaRequest request) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(EventoNaoEncontradoException::new);
        if (evento.getStatus() != StatusEvento.CRIADO) {
            throw new StatusInvalidoParaAlteracaoException();
        }
        Participante participante = participanteRepository.findByParticipanteIdAndEventoEventoId(participanteId, eventoId)
                .orElseThrow(ParticipanteNaoEncontradoException::new);
        participante.setPresente(request.presente());
        Participante participanteAtualizado = participanteRepository.save(participante);
        return toParticipanteResponse(participanteAtualizado);
    }

    private Participante vincularSolicitacao(Evento evento, UUID solicitacaoId) {
        if (evento.getStatus() != StatusEvento.CRIADO) {
            throw new StatusInvalidoParaAlteracaoException();
        }
        SolicitacaoResponse solicitacao = buscarSolicitacao(solicitacaoId);
        if (solicitacao.status() != StatusSolicitacao.APROVADA) {
            throw new SolicitacaoNaoAprovadaException();
        }
        if (participanteRepository.existsBySolicitacaoIdAndEventoStatus(solicitacaoId, StatusEvento.CRIADO)) {
            throw new SolicitacaoJaVinculadaException();
        }
        Participante participante = new Participante();
        participante.setEvento(evento);
        participante.setSolicitacaoId(solicitacaoId);
        participante.setGestanteId(solicitacao.gestanteId());
        return participanteRepository.save(participante);
    }

    private SolicitacaoResponse buscarSolicitacao(UUID solicitacaoId) {
        try {
            return solicitacaoClient.findById(solicitacaoId);
        } catch (FeignException.NotFound exception) {
            throw new SolicitacaoNaoEncontradaException();
        }
    }

    private ParticipanteResponse toParticipanteResponse(Participante participante) {
        String nomeGestante = gestanteClient.findById(participante.getGestanteId()).nome();
        return new ParticipanteResponse(
                participante.getParticipanteId(),
                participante.getSolicitacaoId(),
                participante.getGestanteId(),
                nomeGestante,
                participante.getPresente()
        );
    }

}
