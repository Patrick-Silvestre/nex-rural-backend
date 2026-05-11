package com.agromach.dto;

import com.agromach.entity.StatusMaquina;
import com.agromach.entity.TipoMaquina;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * DTO de Maquina. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class MaquinaDto {

    private MaquinaDto() {
    }

    public record MaquinaRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotNull(message = "Tipo e obrigatorio")
        TipoMaquina tipo,

        @NotNull(message = "Status e obrigatorio")
        StatusMaquina status,

        @NotNull(message = "Ano e obrigatorio")
        @PositiveOrZero(message = "Ano invalido")
        Integer ano,

        @NotNull(message = "Valor estimado e obrigatorio")
        @Positive(message = "Valor estimado deve ser positivo")
        BigDecimal valorEstimado,

        @NotNull(message = "ID da fazenda e obrigatorio")
        Long fazendaId
    ) {
    }

    public record MaquinaResponse(
        Long id,
        String nome,
        TipoMaquina tipo,
        StatusMaquina status,
        Integer ano,
        BigDecimal valorEstimado,
        Long fazendaId,
        String fazendaNome
    ) {
    }
}
