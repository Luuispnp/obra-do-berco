package com.github.luuispnp.obra_do_berco_auth.exception;

public class SessaoInvalidaException extends RuntimeException {
    public SessaoInvalidaException() {
        super("Sessão inválida.");
    }
}
