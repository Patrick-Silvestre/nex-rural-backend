package com.agromach.dto;

import com.agromach.entity.TipoAreaProducao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

/**
 * DTO de AreaProducao. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class AreaProducaoDto {

    private AreaProducaoDto() {
    }

    public record AreaProducaoRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotNull(message = "Tipo e obrigatorio")
        TipoAreaProducao tipo,

        @Positive(message = "Tamanho em hectares deve ser positivo")
        Double tamanhoHectares,

        String ocupacaoDescricao,

        @PositiveOrZero(message = "Quantidade de animais invalida")
        Integer quantidadeAnimais,

        String culturaAtual,

        @NotNull(message = "ID da fazenda e obrigatorio")
        Long fazendaId
    ) {
    }

    public record AreaProducaoResponse(
        Long id,
        String nome,
        TipoAreaProducao tipo,
        Double tamanhoHectares,
        String ocupacaoDescricao,
        Integer quantidadeAnimais,
        String culturaAtual,
        LocalDateTime atualizadoEm,
        Long fazendaId,
        String fazendaNome
    ) {
    }
}
