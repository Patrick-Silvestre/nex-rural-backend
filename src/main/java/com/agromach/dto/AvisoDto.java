package com.agromach.dto;

import com.agromach.entity.TipoAviso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO de Aviso. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class AvisoDto {

    private AvisoDto() {
    }

    public record AvisoRequest(
        @NotNull(message = "Tipo e obrigatorio")
        TipoAviso tipo,

        @NotBlank(message = "Descricao e obrigatoria")
        String descricao,

        @NotNull(message = "Data prevista e obrigatoria")
        LocalDate dataPrevista,

        boolean concluido,

        @NotNull(message = "ID da fazenda e obrigatorio")
        Long fazendaId,

        Long areaProducaoId
    ) {
    }

    public record AvisoResponse(
        Long id,
        TipoAviso tipo,
        String descricao,
        LocalDate dataPrevista,
        boolean concluido,
        Long fazendaId,
        Long areaProducaoId,
        String areaProducaoNome
    ) {
    }
}
