package com.agromach.controller;

import com.agromach.dto.AreaProducaoDto;
import com.agromach.service.AreaProducaoService;
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
 * Controller REST de AreaProducao. Recebe requisicoes HTTP, valida dados de entrada e delega regras para a camada de servico.
 */
@RestController
@RequestMapping(ApiPaths.AREAS_PRODUCAO)
@RequiredArgsConstructor
@Tag(name = "Areas de Producao")
public class AreaProducaoController {

    private final AreaProducaoService areaProducaoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<AreaProducaoDto.AreaProducaoResponse> create(@Valid @RequestBody AreaProducaoDto.AreaProducaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaProducaoService.create(request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AreaProducaoDto.AreaProducaoResponse>> findAll() {
        return ResponseEntity.ok(areaProducaoService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AreaProducaoDto.AreaProducaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(areaProducaoService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<AreaProducaoDto.AreaProducaoResponse> update(@PathVariable Long id,
                                                                       @Valid @RequestBody AreaProducaoDto.AreaProducaoRequest request) {
        return ResponseEntity.ok(areaProducaoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        areaProducaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
