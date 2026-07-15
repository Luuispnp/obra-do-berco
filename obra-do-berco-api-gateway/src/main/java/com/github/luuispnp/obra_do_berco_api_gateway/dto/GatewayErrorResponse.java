package com.github.luuispnp.obra_do_berco_api_gateway.dto;

public record GatewayErrorResponse(GatewayError error) {

    public record GatewayError(String code, String message) {
    }

    public static GatewayErrorResponse unauthorized(String message) {
        return new GatewayErrorResponse(new GatewayError("UNAUTHORIZED", message));
    }

}
