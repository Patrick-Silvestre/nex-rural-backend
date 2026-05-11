package com.agromach.exception;

/**
 * Excecao para recurso nao encontrado. Usada quando um registro esperado nao existe no banco.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
