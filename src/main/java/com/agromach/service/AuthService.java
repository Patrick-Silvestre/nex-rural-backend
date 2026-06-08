package com.agromach.service;

import com.agromach.dto.AuthDto;
import com.agromach.dto.UsuarioDto;
import com.agromach.entity.Fazenda;
import com.agromach.entity.Role;
import com.agromach.entity.Usuario;
import com.agromach.exception.BusinessException;
import com.agromach.repository.UsuarioRepository;
import com.agromach.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Auth. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email ja cadastrado");
        }

        // Perfil padrao para auto cadastro quando o cliente nao informa role.
        Role role = request.role() != null ? request.role() : Role.CLIENTE;

        Usuario usuario = Usuario.builder()
            .nome(request.nome())
            .email(request.email())
            .senha(passwordEncoder.encode(request.senha()))
            .telefone(request.telefone())
            .documento(request.documento())
            .role(role)
            .build();

        Usuario salvo = usuarioRepository.save(usuario);
        String token = jwtService.generateToken(salvo);

        return new AuthDto.AuthResponse(token, "Bearer", toUsuarioResponse(salvo));
    }

    /**
     * Executa o fluxo de autenticacao e devolve os dados de sessao/token quando valido.
     */
    @Transactional(readOnly = true)
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
            .orElseThrow(() -> new BusinessException("Credenciais invalidas"));

        String token = jwtService.generateToken(usuario);

        return new AuthDto.AuthResponse(token, "Bearer", toUsuarioResponse(usuario));
    }

    /**
     * Converte ou extrai dados entre formatos/estruturas usadas no sistema.
     */
    private UsuarioDto.UsuarioResponse toUsuarioResponse(Usuario usuario) {
        return new UsuarioDto.UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getDocumento(),
            usuario.getRole(),
            getFazendaId(usuario),
            getFazendaNome(usuario)
        );
    }

    /**
     * Retorna o ID da fazenda vinculada ao usuario no relacionamento 1:1.
     */
    private Long getFazendaId(Usuario usuario) {
        Fazenda fazenda = usuario.getFazenda();
        return fazenda != null ? fazenda.getId() : null;
    }

    /**
     * Retorna o nome da fazenda vinculada ao usuario no relacionamento 1:1.
     */
    private String getFazendaNome(Usuario usuario) {
        Fazenda fazenda = usuario.getFazenda();
        return fazenda != null ? fazenda.getNome() : null;
    }
}
