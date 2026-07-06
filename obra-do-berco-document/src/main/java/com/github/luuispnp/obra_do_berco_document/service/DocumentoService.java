package com.github.luuispnp.obra_do_berco_document.service;

import com.github.luuispnp.obra_do_berco_document.client.GestanteClient;
import com.github.luuispnp.obra_do_berco_document.client.SolicitacaoClient;
import com.github.luuispnp.obra_do_berco_document.dto.response.GestanteResponse;
import com.github.luuispnp.obra_do_berco_document.dto.response.SolicitacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

    @Autowired
    private GestanteClient gestanteClient;

    @Autowired
    private SolicitacaoClient solicitacaoClient;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public ResponseEntity<byte[]> gerarPdf(Long solicitacaoId) {

        SolicitacaoResponse solicitacao =
                solicitacaoClient.getSolicitacaoById(solicitacaoId).getBody();

        GestanteResponse gestante =
                gestanteClient.getGestanteById(solicitacao.getGestanteId()).getBody();

        byte[] pdf =
                pdfGeneratorService.gerarPdf(gestante, solicitacao);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=ficha-gestante.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
