package com.github.luuispnp.obra_do_berco_document.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity
                .internalServerError()
                .body(error);
    }

    @ExceptionHandler(SolicitacaoNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleSolicitacaoNaoEncontradaException(SolicitacaoNaoEncontradaException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(EventoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleEventoNaoEncontradoException(EventoNaoEncontradoException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

}
