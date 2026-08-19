package com.agromach.util;

/**
 * Classe utilitaria de rotas. Centraliza os caminhos base dos endpoints para evitar duplicacao de strings.
 */
public final class ApiPaths {

    private ApiPaths() {
    }

    public static final String AUTH = "/auth";
    public static final String USUARIOS = "/api/usuarios";
    public static final String FAZENDAS = "/api/fazendas";
    public static final String MAQUINAS = "/api/maquinas";
    public static final String FUNCIONARIOS = "/api/funcionarios";
    public static final String AREAS_PRODUCAO = "/api/areas-producao";
    public static final String AVISOS = "/api/avisos";
    public static final String PROFISSIONAIS = "/api/profissionais";
    public static final String DASHBOARD = "/api/dashboard";
}
