package com.agromach.controller;

import com.agromach.dto.MaquinaDto;
import com.agromach.service.MaquinaService;
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
 * Controller REST de Maquina. Recebe requisicoes HTTP, valida dados de entrada e delega regras para a camada de servico.
 */
@RestController
@RequestMapping(ApiPaths.MAQUINAS)
@RequiredArgsConstructor
@Tag(name = "Maquinas")
public class MaquinaController {

    private final MaquinaService maquinaService;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MaquinaDto.MaquinaResponse> create(@Valid @RequestBody MaquinaDto.MaquinaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maquinaService.create(request));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MaquinaDto.MaquinaResponse>> findAll() {
        return ResponseEntity.ok(maquinaService.findAll());
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MaquinaDto.MaquinaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(maquinaService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MaquinaDto.MaquinaResponse> update(@PathVariable Long id,
                                                             @Valid @RequestBody MaquinaDto.MaquinaRequest request) {
        return ResponseEntity.ok(maquinaService.update(id, request));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maquinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
