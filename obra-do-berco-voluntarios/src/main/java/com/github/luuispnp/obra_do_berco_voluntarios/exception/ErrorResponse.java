package com.github.luuispnp.obra_do_berco_voluntarios.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timestamp,
    String message,
    Integer status
) {}
