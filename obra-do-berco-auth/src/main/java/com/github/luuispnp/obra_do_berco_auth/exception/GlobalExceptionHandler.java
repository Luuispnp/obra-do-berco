package com.github.luuispnp.obra_do_berco_auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity
                .badRequest()
                .body(error);
    }

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

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredenciaisInvalidasException(CredenciaisInvalidasException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(RefreshTokenInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenInvalidoException(RefreshTokenInvalidoException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(SessaoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleSessaoInvalidaException(SessaoInvalidaException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

}
