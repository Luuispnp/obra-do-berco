package com.github.luuispnp.obra_do_berco_document.exception;

public class SolicitacaoNaoEncontradaException extends RuntimeException {
    public SolicitacaoNaoEncontradaException() {
        super("Solicitação não encontrada.");
    }
}
