package com.agromach.controller;

import com.agromach.dto.AvisoDto;
import com.agromach.service.AvisoService;
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
 * Controller REST de Aviso. Recebe requisicoes HTTP, valida dados de entrada e delega regras para a camada de servico.
 */
@RestController
@RequestMapping(ApiPaths.AVISOS)
@RequiredArgsConstructor
@Tag(name = "Avisos")
public class AvisoController {

    private final AvisoService avisoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<AvisoDto.AvisoResponse> create(@Valid @RequestBody AvisoDto.AvisoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(avisoService.create(request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AvisoDto.AvisoResponse>> findAll() {
        return ResponseEntity.ok(avisoService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AvisoDto.AvisoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(avisoService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<AvisoDto.AvisoResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody AvisoDto.AvisoRequest request) {
        return ResponseEntity.ok(avisoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        avisoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
