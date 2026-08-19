package com.agromach.service;

import com.agromach.dto.AreaProducaoDto;
import com.agromach.entity.AreaProducao;
import com.agromach.entity.Fazenda;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.AreaProducaoRepository;
import com.agromach.repository.FazendaRepository;
import com.agromach.security.SecurityUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de AreaProducao. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class AreaProducaoService {

    private final AreaProducaoRepository areaProducaoRepository;
    private final FazendaRepository fazendaRepository;

    @Transactional
    public AreaProducaoDto.AreaProducaoResponse create(AreaProducaoDto.AreaProducaoRequest request) {
        Fazenda fazenda = findFazendaById(request.fazendaId());
        SecurityUtils.requireOwnerOrAdmin(fazenda.getProprietario().getId());

        AreaProducao area = AreaProducao.builder()
            .nome(request.nome())
            .tipo(request.tipo())
            .tamanhoHectares(request.tamanhoHectares())
            .ocupacaoDescricao(request.ocupacaoDescricao())
            .quantidadeAnimais(request.quantidadeAnimais())
            .culturaAtual(request.culturaAtual())
            .atualizadoEm(LocalDateTime.now())
            .fazenda(fazenda)
            .build();

        return toResponse(areaProducaoRepository.save(area));
    }

    @Transactional(readOnly = true)
    public List<AreaProducaoDto.AreaProducaoResponse> findAll() {
        return areaProducaoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AreaProducaoDto.AreaProducaoResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public AreaProducaoDto.AreaProducaoResponse update(Long id, AreaProducaoDto.AreaProducaoRequest request) {
        AreaProducao area = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(area.getFazenda().getProprietario().getId());
        Fazenda fazenda = findFazendaById(request.fazendaId());
        SecurityUtils.requireOwnerOrAdmin(fazenda.getProprietario().getId());

        area.setNome(request.nome());
        area.setTipo(request.tipo());
        area.setTamanhoHectares(request.tamanhoHectares());
        area.setOcupacaoDescricao(request.ocupacaoDescricao());
        area.setQuantidadeAnimais(request.quantidadeAnimais());
        area.setCulturaAtual(request.culturaAtual());
        area.setAtualizadoEm(LocalDateTime.now());
        area.setFazenda(fazenda);

        return toResponse(areaProducaoRepository.save(area));
    }

    @Transactional
    public void delete(Long id) {
        AreaProducao area = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(area.getFazenda().getProprietario().getId());
        areaProducaoRepository.delete(area);
    }

    private AreaProducao findEntityById(Long id) {
        return areaProducaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Area de producao nao encontrada com ID " + id));
    }

    private Fazenda findFazendaById(Long id) {
        return fazendaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fazenda nao encontrada com ID " + id));
    }

    AreaProducaoDto.AreaProducaoResponse toResponse(AreaProducao area) {
        return new AreaProducaoDto.AreaProducaoResponse(
            area.getId(),
            area.getNome(),
            area.getTipo(),
            area.getTamanhoHectares(),
            area.getOcupacaoDescricao(),
            area.getQuantidadeAnimais(),
            area.getCulturaAtual(),
            area.getAtualizadoEm(),
            area.getFazenda().getId(),
            area.getFazenda().getNome()
        );
    }
}
