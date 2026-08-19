package com.agromach.controller;

import com.agromach.dto.DashboardDto;
import com.agromach.service.DashboardService;
import com.agromach.util.ApiPaths;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST de Dashboard. Agrega, para uma fazenda, os dados operacionais exibidos no painel inicial.
 */
@RestController
@RequestMapping(ApiPaths.DASHBOARD)
@RequiredArgsConstructor
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    // Dashboard da primeira fazenda do usuario autenticado (caso comum: um produtor, uma fazenda).
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardDto.DashboardResponse> dashboardAtual() {
        return ResponseEntity.ok(dashboardService.obterDashboardAtual());
    }

    @GetMapping("/{fazendaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardDto.DashboardResponse> dashboardPorFazenda(@PathVariable Long fazendaId) {
        return ResponseEntity.ok(dashboardService.obterDashboard(fazendaId));
    }
}
