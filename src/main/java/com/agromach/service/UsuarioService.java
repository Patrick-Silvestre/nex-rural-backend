package com.agromach.service;

import com.agromach.dto.UsuarioDto;
import com.agromach.entity.Role;
import com.agromach.entity.Usuario;
import com.agromach.exception.BusinessException;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Servico de negocio de Usuario. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public UsuarioDto.UsuarioResponse create(UsuarioDto.UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email ja cadastrado");
        }

        Role role = request.role() != null ? request.role() : Role.CLIENTE;

        Usuario usuario = Usuario.builder()
            .nome(request.nome())
            .email(request.email())
            .senha(passwordEncoder.encode(request.senha()))
            .telefone(request.telefone())
            .documento(request.documento())
            .role(role)
            .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public List<UsuarioDto.UsuarioResponse> findAll() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public UsuarioDto.UsuarioResponse findById(Long id) {
        Usuario usuario = findEntityById(id);
        return toResponse(usuario);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public UsuarioDto.UsuarioResponse update(Long id, UsuarioDto.UsuarioUpdateRequest request) {
        Usuario usuario = findEntityById(id);

        if (!usuario.getEmail().equals(request.email()) && usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email ja cadastrado");
        }

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());
        usuario.setDocumento(request.documento());
        usuario.setRole(request.role() != null ? request.role() : usuario.getRole());

        if (StringUtils.hasText(request.senha())) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }

        return toResponse(usuarioRepository.save(usuario));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @Transactional
    public void delete(Long id) {
        Usuario usuario = findEntityById(id);
        usuarioRepository.delete(usuario);
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Usuario findEntityById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com ID " + id));
    }

    /**
     * Converte ou extrai dados entre formatos/estruturas usadas no sistema.
     */
    private UsuarioDto.UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioDto.UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getDocumento(),
            usuario.getRole()
        );
    }
}
