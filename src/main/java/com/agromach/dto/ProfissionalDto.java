package com.agromach.dto;

import com.agromach.entity.TipoProfissional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Profissional. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class ProfissionalDto {

    private ProfissionalDto() {
    }

    public record ProfissionalRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotNull(message = "Tipo e obrigatorio")
        TipoProfissional tipo,

        @NotBlank(message = "Telefone e obrigatorio")
        String telefone,

        String descricao,

        boolean rastreabilidade,

        String cidadeRegiao
    ) {
    }

    public record ProfissionalResponse(
        Long id,
        String nome,
        TipoProfissional tipo,
        String telefone,
        String descricao,
        boolean rastreabilidade,
        String cidadeRegiao,
        Long cadastradoPorId,
        String cadastradoPorNome
    ) {
    }
}
