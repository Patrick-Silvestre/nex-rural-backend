package com.agromach.dto;

import com.agromach.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de Usuario. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class UsuarioDto {

    private UsuarioDto() {
    }

    public record UsuarioRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @NotBlank(message = "Senha e obrigatoria")
        @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
        String senha,

        String telefone,
        String documento,
        Role role
    ) {
    }

    public record UsuarioUpdateRequest(
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
        String senha,

        String telefone,
        String documento,
        Role role
    ) {
    }

    public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String documento,
        Role role,
        Long fazendaId,
        String fazendaNome
    ) {
    }
}
