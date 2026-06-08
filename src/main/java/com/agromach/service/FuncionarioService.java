package com.agromach.service;

import com.agromach.dto.FuncionarioDto;
import com.agromach.entity.Fazenda;
import com.agromach.entity.Funcionario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.FazendaRepository;
import com.agromach.repository.FuncionarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        return createWithFazenda(request.nome(), request.cargo(), request.salario(), request.dataAdmissao(), fazenda);
    }

    /**
     * Cria funcionario dentro de uma fazenda especifica para o CRUD 1:N Fazenda -> Funcionarios.
     */
    @Transactional
    public FuncionarioDto.FuncionarioResponse createForFazenda(Long fazendaId, FuncionarioDto.FuncionarioFazendaRequest request) {
        Fazenda fazenda = findFazendaById(fazendaId);
        return createWithFazenda(request.nome(), request.cargo(), request.salario(), request.dataAdmissao(), fazenda);
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
     * Lista funcionarios de uma fazenda para representar o lado N do relacionamento 1:N.
     */
    @Transactional(readOnly = true)
    public List<FuncionarioDto.FuncionarioResponse> findAllByFazenda(Long fazendaId) {
        findFazendaById(fazendaId);
        return funcionarioRepository.findByFazendaId(fazendaId).stream().map(this::toResponse).toList();
    }

    /**
     * Busca um funcionario dentro de uma fazenda especifica.
     */
    @Transactional(readOnly = true)
    public FuncionarioDto.FuncionarioResponse findByFazenda(Long fazendaId, Long funcionarioId) {
        Funcionario funcionario = findEntityByIdAndFazendaId(funcionarioId, fazendaId);
        return toResponse(funcionario);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public FuncionarioDto.FuncionarioResponse update(Long id, FuncionarioDto.FuncionarioRequest request) {
        Funcionario funcionario = findEntityById(id);
        Fazenda fazenda = findFazendaById(request.fazendaId());
        updateFields(funcionario, request.nome(), request.cargo(), request.salario(), request.dataAdmissao(), fazenda);
        return toResponse(funcionarioRepository.save(funcionario));
    }

    /**
     * Atualiza funcionario mantendo-o dentro da fazenda da rota 1:N.
     */
    @Transactional
    public FuncionarioDto.FuncionarioResponse updateForFazenda(Long fazendaId, Long funcionarioId,
                                                               FuncionarioDto.FuncionarioFazendaRequest request) {
        Funcionario funcionario = findEntityByIdAndFazendaId(funcionarioId, fazendaId);
        updateFields(funcionario, request.nome(), request.cargo(), request.salario(), request.dataAdmissao(), funcionario.getFazenda());
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
     * Remove funcionario garantindo que ele pertence a fazenda da rota 1:N.
     */
    @Transactional
    public void deleteFromFazenda(Long fazendaId, Long funcionarioId) {
        Funcionario funcionario = findEntityByIdAndFazendaId(funcionarioId, fazendaId);
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
     * Busca funcionario dentro da fazenda indicada pela rota 1:N.
     */
    private Funcionario findEntityByIdAndFazendaId(Long funcionarioId, Long fazendaId) {
        return funcionarioRepository.findByIdAndFazendaId(funcionarioId, fazendaId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Funcionario nao encontrado com ID " + funcionarioId + " na fazenda " + fazendaId));
    }

    /**
     * Cria entidade funcionario usando a fazenda ja carregada.
     */
    private FuncionarioDto.FuncionarioResponse createWithFazenda(String nome, String cargo, BigDecimal salario,
                                                                 LocalDate dataAdmissao, Fazenda fazenda) {
        Funcionario funcionario = Funcionario.builder()
            .nome(nome)
            .cargo(cargo)
            .salario(salario)
            .dataAdmissao(dataAdmissao)
            .fazenda(fazenda)
            .build();

        return toResponse(funcionarioRepository.save(funcionario));
    }

    /**
     * Atualiza campos comuns do funcionario.
     */
    private void updateFields(Funcionario funcionario, String nome, String cargo, BigDecimal salario,
                              LocalDate dataAdmissao, Fazenda fazenda) {
        funcionario.setNome(nome);
        funcionario.setCargo(cargo);
        funcionario.setSalario(salario);
        funcionario.setDataAdmissao(dataAdmissao);
        funcionario.setFazenda(fazenda);
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
