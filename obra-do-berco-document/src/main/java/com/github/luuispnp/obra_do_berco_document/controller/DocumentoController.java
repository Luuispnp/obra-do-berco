package com.github.luuispnp.obra_do_berco_document.controller;

import com.github.luuispnp.obra_do_berco_document.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping("/ficha/{solicitacaoId}")
    public ResponseEntity<byte[]> gerarFichaIndividual(@PathVariable UUID solicitacaoId) {
        byte[] pdf = documentoService.gerarFichaIndividual(solicitacaoId);
        return responderPdf(pdf, "ficha-" + solicitacaoId + ".pdf");
    }

    @GetMapping("/eventos/{eventoId}/lista")
    public ResponseEntity<byte[]> gerarListaEvento(@PathVariable UUID eventoId) {
        byte[] pdf = documentoService.gerarListaEvento(eventoId);
        return responderPdf(pdf, "lista-evento-" + eventoId + ".pdf");
    }

    @GetMapping("/eventos/{eventoId}/fichas")
    public ResponseEntity<byte[]> gerarFichasEvento(@PathVariable UUID eventoId) {
        byte[] pdf = documentoService.gerarFichasEvento(eventoId);
        return responderPdf(pdf, "fichas-evento-" + eventoId + ".pdf");
    }

    private ResponseEntity<byte[]> responderPdf(byte[] pdf, String filename) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(pdf);
    }

}
