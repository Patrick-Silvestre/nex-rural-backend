package com.agromach.service;

import com.agromach.dto.MaquinaDto;
import com.agromach.entity.Fazenda;
import com.agromach.entity.Maquina;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.FazendaRepository;
import com.agromach.repository.MaquinaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Maquina. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;
    private final FazendaRepository fazendaRepository;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public MaquinaDto.MaquinaResponse create(MaquinaDto.MaquinaRequest request) {
        Fazenda fazenda = findFazendaById(request.fazendaId());

        Maquina maquina = Maquina.builder()
            .nome(request.nome())
            .tipo(request.tipo())
            .status(request.status())
            .ano(request.ano())
            .valorEstimado(request.valorEstimado())
            .fazenda(fazenda)
            .build();

        return toResponse(maquinaRepository.save(maquina));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public List<MaquinaDto.MaquinaResponse> findAll() {
        return maquinaRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public MaquinaDto.MaquinaResponse findById(Long id) {
        Maquina maquina = findEntityById(id);
        return toResponse(maquina);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public MaquinaDto.MaquinaResponse update(Long id, MaquinaDto.MaquinaRequest request) {
        Maquina maquina = findEntityById(id);
        Fazenda fazenda = findFazendaById(request.fazendaId());

        maquina.setNome(request.nome());
        maquina.setTipo(request.tipo());
        maquina.setStatus(request.status());
        maquina.setAno(request.ano());
        maquina.setValorEstimado(request.valorEstimado());
        maquina.setFazenda(fazenda);

        return toResponse(maquinaRepository.save(maquina));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @Transactional
    public void delete(Long id) {
        Maquina maquina = findEntityById(id);
        maquinaRepository.delete(maquina);
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Maquina findEntityById(Long id) {
        return maquinaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Maquina nao encontrada com ID " + id));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Fazenda findFazendaById(Long id) {
        return fazendaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fazenda nao encontrada com ID " + id));
    }

    /**
     * Converte ou extrai dados entre formatos/estruturas usadas no sistema.
     */
    private MaquinaDto.MaquinaResponse toResponse(Maquina maquina) {
        return new MaquinaDto.MaquinaResponse(
            maquina.getId(),
            maquina.getNome(),
            maquina.getTipo(),
            maquina.getStatus(),
            maquina.getAno(),
            maquina.getValorEstimado(),
            maquina.getFazenda().getId(),
            maquina.getFazenda().getNome()
        );
    }
}
