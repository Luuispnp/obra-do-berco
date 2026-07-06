package com.github.luuispnp.obra_do_berco_solicitacoes.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String erro;
    private int status;

}
