package com.agromach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de Funcionario. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class FuncionarioDto {

    private FuncionarioDto() {
    }

    public record FuncionarioRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotBlank(message = "Cargo e obrigatorio")
        String cargo,

        @NotNull(message = "Salario e obrigatorio")
        @Positive(message = "Salario deve ser positivo")
        BigDecimal salario,

        @NotNull(message = "Data de admissao e obrigatoria")
        LocalDate dataAdmissao,

        @NotNull(message = "ID da fazenda e obrigatorio")
        Long fazendaId
    ) {
    }

    public record FuncionarioResponse(
        Long id,
        String nome,
        String cargo,
        BigDecimal salario,
        LocalDate dataAdmissao,
        Long fazendaId,
        String fazendaNome
    ) {
    }
}
