package com.agromach.dto;

import com.agromach.entity.StatusPedido;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Pedido. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class PedidoDto {

    private PedidoDto() {
    }

    public record PedidoRequest(
        @NotNull(message = "ID do comprador e obrigatorio")
        Long compradorId,

        @NotNull(message = "ID do produto e obrigatorio")
        Long produtoId,

        StatusPedido status
    ) {
    }

    public record PedidoResponse(
        Long id,
        Long compradorId,
        String compradorNome,
        Long produtoId,
        String produtoTitulo,
        StatusPedido status
    ) {
    }
}
