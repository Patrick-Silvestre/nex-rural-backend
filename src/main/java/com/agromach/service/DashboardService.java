package com.agromach.service;

import com.agromach.dto.DashboardDto;
import com.agromach.entity.Fazenda;
import com.agromach.entity.Maquina;
import com.agromach.entity.StatusMaquina;
import com.agromach.entity.Usuario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.AreaProducaoRepository;
import com.agromach.repository.AvisoRepository;
import com.agromach.repository.FazendaRepository;
import com.agromach.repository.FuncionarioRepository;
import com.agromach.repository.MaquinaRepository;
import com.agromach.security.SecurityUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Dashboard. Agrega, para uma fazenda, os dados operacionais
 * usados pela tela inicial: equipe, maquinario, areas de producao, avisos e clima.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FazendaRepository fazendaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final MaquinaRepository maquinaRepository;
    private final AreaProducaoRepository areaProducaoRepository;
    private final AvisoRepository avisoRepository;
    private final AreaProducaoService areaProducaoService;
    private final AvisoService avisoService;
    private final ClimaService climaService;

    @Transactional(readOnly = true)
    public DashboardDto.DashboardResponse obterDashboardAtual() {
        Usuario usuario = SecurityUtils.currentUser();
        List<Fazenda> fazendas = fazendaRepository.findByProprietarioId(usuario.getId());

        Fazenda fazenda = fazendas.stream()
            .findFirst()
            .or(() -> fazendaRepository.findAll().stream().findFirst())
            .orElseThrow(() -> new ResourceNotFoundException("Nenhuma fazenda cadastrada ainda"));

        return montarDashboard(fazenda);
    }

    @Transactional(readOnly = true)
    public DashboardDto.DashboardResponse obterDashboard(Long fazendaId) {
        Fazenda fazenda = fazendaRepository.findById(fazendaId)
            .orElseThrow(() -> new ResourceNotFoundException("Fazenda nao encontrada com ID " + fazendaId));

        return montarDashboard(fazenda);
    }

    private DashboardDto.DashboardResponse montarDashboard(Fazenda fazenda) {
        SecurityUtils.requireOwnerOrAdmin(fazenda.getProprietario().getId());

        long totalFuncionarios = funcionarioRepository.countByFazendaId(fazenda.getId());
        List<DashboardDto.MaquinaStatusResumo> maquinasPorStatus = resumirMaquinasPorStatus(fazenda.getId());

        List<com.agromach.dto.AreaProducaoDto.AreaProducaoResponse> areas = areaProducaoRepository.findByFazendaId(fazenda.getId())
            .stream()
            .map(areaProducaoService::toResponse)
            .toList();

        List<com.agromach.dto.AvisoDto.AvisoResponse> avisosPendentes = avisoRepository
            .findByFazendaIdAndConcluidoFalseOrderByDataPrevistaAsc(fazenda.getId())
            .stream()
            .map(avisoService::toResponse)
            .toList();

        var clima = climaService.obterClima(fazenda.getLatitude(), fazenda.getLongitude());

        return new DashboardDto.DashboardResponse(
            fazenda.getId(),
            fazenda.getNome(),
            totalFuncionarios,
            maquinasPorStatus,
            areas,
            avisosPendentes,
            clima
        );
    }

    private List<DashboardDto.MaquinaStatusResumo> resumirMaquinasPorStatus(Long fazendaId) {
        Map<StatusMaquina, Long> contagem = maquinaRepository.findByFazendaId(fazendaId).stream()
            .collect(Collectors.groupingBy(Maquina::getStatus, Collectors.counting()));

        return contagem.entrySet().stream()
            .map(entry -> new DashboardDto.MaquinaStatusResumo(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparing(resumo -> resumo.status().name()))
            .toList();
    }
}
