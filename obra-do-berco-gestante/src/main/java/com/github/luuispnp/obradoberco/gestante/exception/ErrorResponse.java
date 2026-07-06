package com.github.luuispnp.obradoberco.gestante.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String erro;
    private int status;

}
