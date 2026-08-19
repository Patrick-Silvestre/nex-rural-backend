package com.agromach.service;

import com.agromach.dto.ProfissionalDto;
import com.agromach.entity.Profissional;
import com.agromach.entity.Usuario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.ProfissionalRepository;
import com.agromach.security.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Profissional. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    @Transactional
    public ProfissionalDto.ProfissionalResponse create(ProfissionalDto.ProfissionalRequest request) {
        Usuario usuario = SecurityUtils.currentUser();

        Profissional profissional = Profissional.builder()
            .nome(request.nome())
            .tipo(request.tipo())
            .telefone(request.telefone())
            .descricao(request.descricao())
            .rastreabilidade(request.rastreabilidade())
            .cidadeRegiao(request.cidadeRegiao())
            .cadastradoPor(usuario)
            .build();

        return toResponse(profissionalRepository.save(profissional));
    }

    @Transactional(readOnly = true)
    public List<ProfissionalDto.ProfissionalResponse> findAll() {
        return profissionalRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProfissionalDto.ProfissionalResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public ProfissionalDto.ProfissionalResponse update(Long id, ProfissionalDto.ProfissionalRequest request) {
        Profissional profissional = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(profissional.getCadastradoPor().getId());

        profissional.setNome(request.nome());
        profissional.setTipo(request.tipo());
        profissional.setTelefone(request.telefone());
        profissional.setDescricao(request.descricao());
        profissional.setRastreabilidade(request.rastreabilidade());
        profissional.setCidadeRegiao(request.cidadeRegiao());

        return toResponse(profissionalRepository.save(profissional));
    }

    @Transactional
    public void delete(Long id) {
        Profissional profissional = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(profissional.getCadastradoPor().getId());
        profissionalRepository.delete(profissional);
    }

    private Profissional findEntityById(Long id) {
        return profissionalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profissional nao encontrado com ID " + id));
    }

    private ProfissionalDto.ProfissionalResponse toResponse(Profissional profissional) {
        return new ProfissionalDto.ProfissionalResponse(
            profissional.getId(),
            profissional.getNome(),
            profissional.getTipo(),
            profissional.getTelefone(),
            profissional.getDescricao(),
            profissional.isRastreabilidade(),
            profissional.getCidadeRegiao(),
            profissional.getCadastradoPor().getId(),
            profissional.getCadastradoPor().getNome()
        );
    }
}
