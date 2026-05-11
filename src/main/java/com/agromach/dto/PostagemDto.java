package com.agromach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO de Postagem. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class PostagemDto {

    private PostagemDto() {
    }

    public record PostagemRequest(
        @NotBlank(message = "Titulo e obrigatorio")
        String titulo,

        @NotBlank(message = "Conteudo e obrigatorio")
        String conteudo,

        @NotNull(message = "ID do autor e obrigatorio")
        Long autorId
    ) {
    }

    public record PostagemResponse(
        Long id,
        String titulo,
        String conteudo,
        Long autorId,
        String autorNome,
        LocalDateTime dataCriacao
    ) {
    }
}
