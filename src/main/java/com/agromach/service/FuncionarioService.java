package com.agromach.service;

import com.agromach.dto.FuncionarioDto;
import com.agromach.entity.Fazenda;
import com.agromach.entity.Funcionario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.FazendaRepository;
import com.agromach.repository.FuncionarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Funcionario. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FazendaRepository fazendaRepository;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public FuncionarioDto.FuncionarioResponse create(FuncionarioDto.FuncionarioRequest request) {
        Fazenda fazenda = findFazendaById(request.fazendaId());

        Funcionario funcionario = Funcionario.builder()
            .nome(request.nome())
            .cargo(request.cargo())
            .salario(request.salario())
            .dataAdmissao(request.dataAdmissao())
            .fazenda(fazenda)
            .build();

        return toResponse(funcionarioRepository.save(funcionario));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public List<FuncionarioDto.FuncionarioResponse> findAll() {
        return funcionarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public FuncionarioDto.FuncionarioResponse findById(Long id) {
        Funcionario funcionario = findEntityById(id);
        return toResponse(funcionario);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public FuncionarioDto.FuncionarioResponse update(Long id, FuncionarioDto.FuncionarioRequest request) {
        Funcionario funcionario = findEntityById(id);
        Fazenda fazenda = findFazendaById(request.fazendaId());

        funcionario.setNome(request.nome());
        funcionario.setCargo(request.cargo());
        funcionario.setSalario(request.salario());
        funcionario.setDataAdmissao(request.dataAdmissao());
        funcionario.setFazenda(fazenda);

        return toResponse(funcionarioRepository.save(funcionario));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @Transactional
    public void delete(Long id) {
        Funcionario funcionario = findEntityById(id);
        funcionarioRepository.delete(funcionario);
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Funcionario findEntityById(Long id) {
        return funcionarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Funcionario nao encontrado com ID " + id));
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
    private FuncionarioDto.FuncionarioResponse toResponse(Funcionario funcionario) {
        return new FuncionarioDto.FuncionarioResponse(
            funcionario.getId(),
            funcionario.getNome(),
            funcionario.getCargo(),
            funcionario.getSalario(),
            funcionario.getDataAdmissao(),
            funcionario.getFazenda().getId(),
            funcionario.getFazenda().getNome()
        );
    }
}
