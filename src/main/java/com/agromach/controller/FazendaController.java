package com.agromach.controller;

import com.agromach.dto.FazendaDto;
import com.agromach.service.FazendaService;
import com.agromach.util.ApiPaths;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST de Fazenda. Recebe requisicoes HTTP, valida dados de entrada e delega regras para a camada de servico.
 */
@RestController
@RequestMapping(ApiPaths.FAZENDAS)
@RequiredArgsConstructor
@Tag(name = "Fazendas")
public class FazendaController {

    private final FazendaService fazendaService;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<FazendaDto.FazendaResponse> create(@Valid @RequestBody FazendaDto.FazendaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fazendaService.create(request));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FazendaDto.FazendaResponse>> findAll() {
        return ResponseEntity.ok(fazendaService.findAll());
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FazendaDto.FazendaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fazendaService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<FazendaDto.FazendaResponse> update(@PathVariable Long id,
                                                             @Valid @RequestBody FazendaDto.FazendaRequest request) {
        return ResponseEntity.ok(fazendaService.update(id, request));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fazendaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
