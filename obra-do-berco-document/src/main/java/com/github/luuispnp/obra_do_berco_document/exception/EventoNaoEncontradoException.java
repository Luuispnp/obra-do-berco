package com.github.luuispnp.obra_do_berco_document.exception;

public class EventoNaoEncontradoException extends RuntimeException {
    public EventoNaoEncontradoException() {
        super("Evento não encontrado.");
    }
}
