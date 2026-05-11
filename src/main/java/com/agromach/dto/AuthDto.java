package com.agromach.dto;

import com.agromach.entity.Role;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de Auth. Define contratos de entrada e saida usados na comunicacao entre frontend e backend.
 */
public final class AuthDto {

    private AuthDto() {
    }

    public record RegisterRequest(
        @JsonAlias({"name", "nomeCompleto", "fullName"})
        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @JsonAlias({"username", "user", "login"})
        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @JsonAlias({"password", "pass"})
        @NotBlank(message = "Senha e obrigatoria")
        @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
        String senha,

        @JsonAlias({"phone", "celular"})
        String telefone,

        @JsonAlias({"document", "cpf", "cnpj"})
        String documento,

        @JsonAlias({"perfil", "tipoUsuario"})
        Role role
    ) {
    }

    public record LoginRequest(
        @JsonAlias({"username", "user", "login"})
        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @JsonAlias({"password", "pass"})
        @NotBlank(message = "Senha e obrigatoria")
        String senha
    ) {
    }

    public record AuthResponse(
        String token,
        String tokenType,
        UsuarioDto.UsuarioResponse usuario
    ) {
        /**
         * Executa a responsabilidade principal desta operacao no fluxo da aplicacao.
         */
        @JsonProperty("accessToken")
        public String accessToken() {
            return token;
        }

        /**
         * Executa a responsabilidade principal desta operacao no fluxo da aplicacao.
         */
        @JsonProperty("user")
        public UsuarioDto.UsuarioResponse user() {
            return usuario;
        }
    }
}
