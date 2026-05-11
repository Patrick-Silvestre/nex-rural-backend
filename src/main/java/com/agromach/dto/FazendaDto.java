package com.agromach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de Fazenda. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class FazendaDto {

    private FazendaDto() {
    }

    public record FazendaRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotBlank(message = "Localizacao e obrigatoria")
        String localizacao,

        @NotNull(message = "Tamanho em hectares e obrigatorio")
        @Positive(message = "Tamanho em hectares deve ser positivo")
        Double tamanhoHectares,

        @NotBlank(message = "Tipo de producao e obrigatorio")
        String tipoProducao,

        @NotNull(message = "ID do proprietario e obrigatorio")
        Long proprietarioId
    ) {
    }

    public record FazendaResponse(
        Long id,
        String nome,
        String localizacao,
        Double tamanhoHectares,
        String tipoProducao,
        Long proprietarioId,
        String proprietarioNome
    ) {
    }
}
