package com.github.luuispnp.obra_do_berco_auth.exception;

public class RefreshTokenInvalidoException extends RuntimeException {
    public RefreshTokenInvalidoException() {
        super("Refresh token inválido, expirado ou revogado.");
    }
}
