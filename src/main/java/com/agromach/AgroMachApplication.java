package com.agromach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicacao Spring Boot. Inicializa todo o contexto e os componentes do backend.
 */
@SpringBootApplication
public class AgroMachApplication {

    /**
     * Executa a responsabilidade principal desta operacao no fluxo da aplicacao.
     */
    public static void main(String[] args) {
        SpringApplication.run(AgroMachApplication.class, args);
    }
}
