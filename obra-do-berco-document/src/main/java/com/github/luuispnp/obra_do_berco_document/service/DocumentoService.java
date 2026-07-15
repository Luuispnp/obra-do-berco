package com.github.luuispnp.obra_do_berco_document.service;

import com.github.luuispnp.obra_do_berco_document.client.EventoClient;
import com.github.luuispnp.obra_do_berco_document.client.GestanteClient;
import com.github.luuispnp.obra_do_berco_document.client.SolicitacaoClient;
import com.github.luuispnp.obra_do_berco_document.client.VoluntarioClient;
import com.github.luuispnp.obra_do_berco_document.dto.response.EventoResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.GestanteResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.ParticipanteResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.SolicitacaoResponse;
import com.github.luuispnp.obra_do_berco_document.exception.EventoNaoEncontradoException;
import com.github.luuispnp.obra_do_berco_document.exception.SolicitacaoNaoEncontradaException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final GestanteClient gestanteClient;

    private final SolicitacaoClient solicitacaoClient;

    private final EventoClient eventoClient;

    private final VoluntarioClient voluntarioClient;

    private final PdfGeneratorService pdfGeneratorService;

    public byte[] gerarFichaIndividual(UUID solicitacaoId) {
        SolicitacaoResponse solicitacao = buscarSolicitacao(solicitacaoId);
        GestanteResponse gestante = gestanteClient.findById(solicitacao.gestanteId());
        var responsavel = voluntarioClient.findById(solicitacao.voluntarioResponsavelId());
        return pdfGeneratorService.gerarFichaIndividual(gestante, solicitacao, null, responsavel);
    }

    public byte[] gerarListaEvento(UUID eventoId) {
        EventoResponse evento = buscarEvento(eventoId);
        List<ParticipanteResponse> participantes = eventoClient.findParticipantes(eventoId);

        List<PdfGeneratorService.LinhaLista> linhas = participantes.stream()
                .map(participante -> {
                    SolicitacaoResponse solicitacao = buscarSolicitacao(participante.solicitacaoId());
                    GestanteResponse gestante = gestanteClient.findById(participante.gestanteId());
                    return new PdfGeneratorService.LinhaLista(
                            gestante.nome(),
                            gestante.telefone(),
                            solicitacao.idadeGestacionalSemanas(),
                            solicitacao.dataProvavelParto(),
                            solicitacao.sexoCrianca(),
                            solicitacao.observacaoGravidez()
                    );
                })
                .toList();

        return pdfGeneratorService.gerarListaParticipantes(evento, linhas);
    }

    public byte[] gerarFichasEvento(UUID eventoId) {
        EventoResponse evento = buscarEvento(eventoId);
        List<ParticipanteResponse> participantes = eventoClient.findParticipantes(eventoId);

        List<PdfGeneratorService.FichaParticipante> fichas = participantes.stream()
                .map(participante -> {
                    SolicitacaoResponse solicitacao = buscarSolicitacao(participante.solicitacaoId());
                    GestanteResponse gestante = gestanteClient.findById(participante.gestanteId());
                    var responsavel = voluntarioClient.findById(solicitacao.voluntarioResponsavelId());
                    return new PdfGeneratorService.FichaParticipante(gestante, solicitacao, responsavel);
                })
                .toList();

        return pdfGeneratorService.gerarFichasEvento(evento, fichas);
    }

    private SolicitacaoResponse buscarSolicitacao(UUID solicitacaoId) {
        try {
            return solicitacaoClient.findById(solicitacaoId);
        } catch (FeignException.NotFound exception) {
            throw new SolicitacaoNaoEncontradaException();
        }
    }

    private EventoResponse buscarEvento(UUID eventoId) {
        try {
            return eventoClient.findById(eventoId);
        } catch (FeignException.NotFound exception) {
            throw new EventoNaoEncontradoException();
        }
    }

}
