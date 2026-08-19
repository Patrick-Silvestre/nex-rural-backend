package com.agromach.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO de Clima. Representa a leitura atual e a previsao curta obtidas do provedor externo de tempo.
 */
public final class ClimaDto {

    private ClimaDto() {
    }

    public record ClimaAtual(
        Double temperaturaAtual,
        Double precipitacaoMm,
        Double velocidadeVentoKmh,
        String condicao
    ) {
    }

    public record PrevisaoDia(
        LocalDate data,
        Double temperaturaMax,
        Double temperaturaMin,
        Integer probabilidadeChuva
    ) {
    }

    public record ClimaResponse(
        ClimaAtual atual,
        List<PrevisaoDia> previsao
    ) {
    }
}
