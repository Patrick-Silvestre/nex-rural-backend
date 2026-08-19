package com.agromach.service;

import com.agromach.dto.AvisoDto;
import com.agromach.entity.AreaProducao;
import com.agromach.entity.Aviso;
import com.agromach.entity.Fazenda;
import com.agromach.exception.BusinessException;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.AreaProducaoRepository;
import com.agromach.repository.AvisoRepository;
import com.agromach.repository.FazendaRepository;
import com.agromach.security.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Aviso. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class AvisoService {

    private final AvisoRepository avisoRepository;
    private final FazendaRepository fazendaRepository;
    private final AreaProducaoRepository areaProducaoRepository;

    @Transactional
    public AvisoDto.AvisoResponse create(AvisoDto.AvisoRequest request) {
        Fazenda fazenda = findFazendaById(request.fazendaId());
        SecurityUtils.requireOwnerOrAdmin(fazenda.getProprietario().getId());
        AreaProducao area = resolveAreaProducao(request.areaProducaoId(), fazenda.getId());

        Aviso aviso = Aviso.builder()
            .tipo(request.tipo())
            .descricao(request.descricao())
            .dataPrevista(request.dataPrevista())
            .concluido(request.concluido())
            .fazenda(fazenda)
            .areaProducao(area)
            .build();

        return toResponse(avisoRepository.save(aviso));
    }

    @Transactional(readOnly = true)
    public List<AvisoDto.AvisoResponse> findAll() {
        return avisoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AvisoDto.AvisoResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public AvisoDto.AvisoResponse update(Long id, AvisoDto.AvisoRequest request) {
        Aviso aviso = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(aviso.getFazenda().getProprietario().getId());
        Fazenda fazenda = findFazendaById(request.fazendaId());
        SecurityUtils.requireOwnerOrAdmin(fazenda.getProprietario().getId());
        AreaProducao area = resolveAreaProducao(request.areaProducaoId(), fazenda.getId());

        aviso.setTipo(request.tipo());
        aviso.setDescricao(request.descricao());
        aviso.setDataPrevista(request.dataPrevista());
        aviso.setConcluido(request.concluido());
        aviso.setFazenda(fazenda);
        aviso.setAreaProducao(area);

        return toResponse(avisoRepository.save(aviso));
    }

    @Transactional
    public void delete(Long id) {
        Aviso aviso = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(aviso.getFazenda().getProprietario().getId());
        avisoRepository.delete(aviso);
    }

    private Aviso findEntityById(Long id) {
        return avisoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aviso nao encontrado com ID " + id));
    }

    private Fazenda findFazendaById(Long id) {
        return fazendaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fazenda nao encontrada com ID " + id));
    }

    private AreaProducao resolveAreaProducao(Long areaProducaoId, Long fazendaId) {
        if (areaProducaoId == null) {
            return null;
        }

        AreaProducao area = areaProducaoRepository.findById(areaProducaoId)
            .orElseThrow(() -> new ResourceNotFoundException("Area de producao nao encontrada com ID " + areaProducaoId));

        if (!area.getFazenda().getId().equals(fazendaId)) {
            throw new BusinessException("Area de producao nao pertence a fazenda informada");
        }

        return area;
    }

    AvisoDto.AvisoResponse toResponse(Aviso aviso) {
        AreaProducao area = aviso.getAreaProducao();

        return new AvisoDto.AvisoResponse(
            aviso.getId(),
            aviso.getTipo(),
            aviso.getDescricao(),
            aviso.getDataPrevista(),
            aviso.isConcluido(),
            aviso.getFazenda().getId(),
            area != null ? area.getId() : null,
            area != null ? area.getNome() : null
        );
    }
}
