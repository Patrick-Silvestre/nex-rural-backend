package com.agromach.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum de dominio Role. Define valores permitidos usados por entidades e regras de negocio.
 */
public enum Role {
    CLIENTE,
    PRESTADOR,
    ADMIN;

    /**
     * Executa a responsabilidade principal desta operacao no fluxo da aplicacao.
     */
    @JsonCreator
    public static Role from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return switch (value.trim().toUpperCase()) {
            case "CLIENTE", "CLIENT", "USER" -> CLIENTE;
            case "PRESTADOR", "PROVIDER", "SERVICE_PROVIDER" -> PRESTADOR;
            case "ADMIN", "ADMINISTRADOR" -> ADMIN;
            default -> throw new IllegalArgumentException("Role invalida: " + value);
        };
    }
}
