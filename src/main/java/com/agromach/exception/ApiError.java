package com.agromach.exception;

import java.time.LocalDateTime;

/**
 * Modelo padrao de erro da API. Representa a estrutura devolvida ao cliente quando ocorre excecao.
 */
public record ApiError(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path
) {
}
