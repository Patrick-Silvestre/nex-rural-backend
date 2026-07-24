package com.agromach.service;

import com.agromach.dto.FazendaDto;
import com.agromach.entity.Fazenda;
import com.agromach.entity.Usuario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.FazendaRepository;
import com.agromach.repository.UsuarioRepository;
import com.agromach.security.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Fazenda. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class FazendaService {

    private final FazendaRepository fazendaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public FazendaDto.FazendaResponse create(FazendaDto.FazendaRequest request) {
        SecurityUtils.requireOwnerOrAdmin(request.proprietarioId());
        Usuario proprietario = findUsuarioById(request.proprietarioId());

        Fazenda fazenda = Fazenda.builder()
            .nome(request.nome())
            .localizacao(request.localizacao())
            .tamanhoHectares(request.tamanhoHectares())
            .tipoProducao(request.tipoProducao())
            .proprietario(proprietario)
            .build();

        return toResponse(fazendaRepository.save(fazenda));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public List<FazendaDto.FazendaResponse> findAll() {
        return fazendaRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public FazendaDto.FazendaResponse findById(Long id) {
        Fazenda fazenda = findEntityById(id);
        return toResponse(fazenda);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public FazendaDto.FazendaResponse update(Long id, FazendaDto.FazendaRequest request) {
        Fazenda fazenda = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(fazenda.getProprietario().getId());
        SecurityUtils.requireOwnerOrAdmin(request.proprietarioId());
        Usuario proprietario = findUsuarioById(request.proprietarioId());

        fazenda.setNome(request.nome());
        fazenda.setLocalizacao(request.localizacao());
        fazenda.setTamanhoHectares(request.tamanhoHectares());
        fazenda.setTipoProducao(request.tipoProducao());
        fazenda.setProprietario(proprietario);

        return toResponse(fazendaRepository.save(fazenda));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @Transactional
    public void delete(Long id) {
        Fazenda fazenda = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(fazenda.getProprietario().getId());
        fazendaRepository.delete(fazenda);
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Fazenda findEntityById(Long id) {
        return fazendaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fazenda nao encontrada com ID " + id));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com ID " + id));
    }

    /**
     * Converte ou extrai dados entre formatos/estruturas usadas no sistema.
     */
    private FazendaDto.FazendaResponse toResponse(Fazenda fazenda) {
        return new FazendaDto.FazendaResponse(
            fazenda.getId(),
            fazenda.getNome(),
            fazenda.getLocalizacao(),
            fazenda.getTamanhoHectares(),
            fazenda.getTipoProducao(),
            fazenda.getProprietario().getId(),
            fazenda.getProprietario().getNome()
        );
    }
}
