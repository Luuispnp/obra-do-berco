package com.github.luuispnp.obra_do_berco_document.controller;


import com.github.luuispnp.obra_do_berco_document.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    DocumentoService documentoService;

    @GetMapping("/solicitacoes/{solicitacaoId}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long solicitacaoId) {
        return documentoService.gerarPdf(solicitacaoId);
    }
}