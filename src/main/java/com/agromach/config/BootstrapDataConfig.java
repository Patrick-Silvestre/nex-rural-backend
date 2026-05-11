package com.agromach.config;

import com.agromach.entity.Role;
import com.agromach.entity.Usuario;
import com.agromach.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuracao de bootstrap. Cria dados iniciais de seguranca (admin padrao) para facilitar o primeiro acesso ao sistema.
 */
@Configuration
@RequiredArgsConstructor
public class BootstrapDataConfig {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@agromach.com";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Bean
    public CommandLineRunner seedDefaultAdmin() {
        return args -> {
            if (usuarioRepository.existsByEmail(DEFAULT_ADMIN_EMAIL)) {
                return;
            }

            Usuario admin = Usuario.builder()
                .nome("Administrador")
                .email(DEFAULT_ADMIN_EMAIL)
                .senha(passwordEncoder.encode("admin123"))
                .telefone("11999999999")
                .documento("00000000000")
                .role(Role.ADMIN)
                .build();

            usuarioRepository.save(admin);
        };
    }
}
