package com.agromach.config;

import com.agromach.entity.Role;
import com.agromach.entity.Usuario;
import com.agromach.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuracao de bootstrap. Cria dados iniciais de seguranca (admin padrao) para facilitar o primeiro acesso ao sistema.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BootstrapDataConfig {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@agromach.com";
    private static final String INSECURE_DEFAULT_PASSWORD = "admin123";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.seed-admin}")
    private boolean seedAdmin;

    @Value("${app.bootstrap.admin-password}")
    private String adminPassword;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Bean
    public CommandLineRunner seedDefaultAdmin() {
        return args -> {
            if (!seedAdmin || usuarioRepository.existsByEmail(DEFAULT_ADMIN_EMAIL)) {
                return;
            }

            if (INSECURE_DEFAULT_PASSWORD.equals(adminPassword)) {
                log.warn("Admin padrao ({}) sendo criado com a senha de dev padrao. "
                    + "Defina APP_BOOTSTRAP_ADMIN_PASSWORD (ou APP_BOOTSTRAP_SEED_ADMIN=false) antes de ir para producao.",
                    DEFAULT_ADMIN_EMAIL);
            }

            Usuario admin = Usuario.builder()
                .nome("Administrador")
                .email(DEFAULT_ADMIN_EMAIL)
                .senha(passwordEncoder.encode(adminPassword))
                .telefone("11999999999")
                .documento("00000000000")
                .role(Role.ADMIN)
                .build();

            usuarioRepository.save(admin);
        };
    }
}
