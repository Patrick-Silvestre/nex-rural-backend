package com.agromach.dto;

import com.agromach.entity.StatusMaquina;
import java.util.List;

/**
 * DTO de Dashboard. Agrega, para uma fazenda, os dados operacionais consumidos pela tela inicial:
 * equipe, maquinario por status, areas de producao, avisos pendentes e clima.
 */
public final class DashboardDto {

    private DashboardDto() {
    }

    public record MaquinaStatusResumo(
        StatusMaquina status,
        long quantidade
    ) {
    }

    public record DashboardResponse(
        Long fazendaId,
        String fazendaNome,
        long totalFuncionarios,
        List<MaquinaStatusResumo> maquinasPorStatus,
        List<AreaProducaoDto.AreaProducaoResponse> areasProducao,
        List<AvisoDto.AvisoResponse> avisosPendentes,
        ClimaDto.ClimaResponse clima
    ) {
    }
}
