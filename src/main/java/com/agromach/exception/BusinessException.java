package com.agromach.exception;

/**
 * Excecao de regra de negocio. Sinaliza violacoes de validacoes funcionais da aplicacao.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
