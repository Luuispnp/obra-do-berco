package github.luuispnp.obra_do_berco_eventos.controller;

import github.luuispnp.obra_do_berco_eventos.dto.request.AdicionarParticipanteRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.AdicionarParticipantesLoteRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.EventoRequest;
import github.luuispnp.obra_do_berco_eventos.dto.request.PresencaRequest;
import github.luuispnp.obra_do_berco_eventos.dto.response.EventoResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.GestanteDisponivelResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.ParticipanteResponse;
import github.luuispnp.obra_do_berco_eventos.dto.response.SolicitacaoResponse;
import github.luuispnp.obra_do_berco_eventos.enums.StatusEvento;
import github.luuispnp.obra_do_berco_eventos.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoResponse> createEvento(@RequestBody @Valid EventoRequest request) {
        EventoResponse response = eventoService.createEvento(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventoResponse>> getEventosWithFilter(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) StatusEvento status
            ) {
        List<EventoResponse> response = eventoService.getEventosWithFilter(dataInicio, dataFim, status);
        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/{eventoId}")
    public ResponseEntity<EventoResponse> getEventoById(@PathVariable UUID eventoId) {
        EventoResponse response = eventoService.getEventoById(eventoId);
        return ResponseEntity
                .ok(response);
    }

    @PutMapping("/{eventoId}")
    public ResponseEntity<EventoResponse> updateEventoById(
            @PathVariable UUID eventoId,
            @RequestBody @Valid EventoRequest request
    ) {
        EventoResponse response = eventoService.updateById(eventoId, request);
        return ResponseEntity
                .ok(response);
    }

    @DeleteMapping("/{eventoId}")
    public ResponseEntity<EventoResponse> deleteById(@PathVariable UUID eventoId) {
        EventoResponse response = eventoService.deleteById(eventoId);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{eventoId}/finalizar")
    public ResponseEntity<Void> finalizarEvento(@PathVariable UUID eventoId) {
        eventoService.finalizarEvento(eventoId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/gestantes-disponiveis")
    public ResponseEntity<List<GestanteDisponivelResponse>> getGestantesDisponiveis() {
        List<GestanteDisponivelResponse> response = eventoService.getGestantesDisponiveis();
        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/{eventoId}/participantes")
    public ResponseEntity<List<ParticipanteResponse>> getParticipantes(@PathVariable UUID eventoId) {
        List<ParticipanteResponse> response = eventoService.getParticipantes(eventoId);
        return ResponseEntity
                .ok(response);
    }

    @PostMapping("/{eventoId}/participantes")
    public ResponseEntity<ParticipanteResponse> adicionarParticipante(
            @PathVariable UUID eventoId,
            @RequestBody @Valid AdicionarParticipanteRequest request) {
        ParticipanteResponse response = eventoService.adicionarParticipante(eventoId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{eventoId}/participantes/lote")
    public ResponseEntity<List<ParticipanteResponse>> adicionarParticipantesLote(
            @PathVariable UUID eventoId,
            @RequestBody @Valid AdicionarParticipantesLoteRequest request) {
        List<ParticipanteResponse> response = eventoService.adicionarParticipantesLote(eventoId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{eventoId}/participantes/{participanteId}")
    public ResponseEntity<ParticipanteResponse> removerParticipante(
            @PathVariable UUID eventoId,
            @PathVariable UUID participanteId) {
        ParticipanteResponse response = eventoService.removerParticipante(eventoId, participanteId);
        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/{eventoId}/participantes/{participanteId}/presenca")
    public ResponseEntity<ParticipanteResponse> atualizarPresenca(
            @PathVariable UUID eventoId,
            @PathVariable UUID participanteId,
            @RequestBody @Valid PresencaRequest request) {
        ParticipanteResponse response = eventoService.atualizarPresenca(eventoId, participanteId, request);
        return ResponseEntity
                .ok(response);
    }

}
