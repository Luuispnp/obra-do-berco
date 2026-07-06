package com.github.luuispnp.obra_do_berco_voluntarios.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse (

    LocalDateTime timestamp,
    List<String> errors,
    Integer status

){}
