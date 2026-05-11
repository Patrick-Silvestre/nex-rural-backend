package com.agromach.dto;

import com.agromach.entity.CategoriaProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO de ProdutoMarketplace. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class ProdutoMarketplaceDto {

    private ProdutoMarketplaceDto() {
    }

    public record ProdutoMarketplaceRequest(
        @NotBlank(message = "Titulo e obrigatorio")
        String titulo,

        @NotBlank(message = "Descricao e obrigatoria")
        String descricao,

        @NotNull(message = "Categoria e obrigatoria")
        CategoriaProduto categoria,

        @NotNull(message = "Preco e obrigatorio")
        @Positive(message = "Preco deve ser positivo")
        BigDecimal preco,

        @NotNull(message = "ID do vendedor e obrigatorio")
        Long vendedorId
    ) {
    }

    public record ProdutoMarketplaceResponse(
        Long id,
        String titulo,
        String descricao,
        CategoriaProduto categoria,
        BigDecimal preco,
        Long vendedorId,
        String vendedorNome
    ) {
    }
}
