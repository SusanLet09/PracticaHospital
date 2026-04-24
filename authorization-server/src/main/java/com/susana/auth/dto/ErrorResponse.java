package com.susana.auth.dto;

public record ErrorResponse(
        int codigo,
        String mensaje
) { }
