package com.github.luuispnp.obra_do_berco_auth.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String erro;
    private int status;

}
