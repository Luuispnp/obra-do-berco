package com.github.luuispnp.obra_do_berco_document.exception;

import java.util.List;

public record ErrorResponse(Error error) {

    public record Error(String code, String message, List<Detail> details) {
        public Error(String code, String message) {
            this(code, message, List.of());
        }
    }

    public record Detail(String field, String message) {
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(new Error(code, message));
    }

    public static ErrorResponse of(String code, String message, List<Detail> details) {
        return new ErrorResponse(new Error(code, message, details));
    }

}
