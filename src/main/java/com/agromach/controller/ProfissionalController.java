package com.agromach.controller;

import com.agromach.dto.ProfissionalDto;
import com.agromach.service.ProfissionalService;
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
 * Controller REST de Profissional. Diretorio de veterinarios, agronomos, fornecedores e trabalhadores de campo.
 */
@RestController
@RequestMapping(ApiPaths.PROFISSIONAIS)
@RequiredArgsConstructor
@Tag(name = "Profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ProfissionalDto.ProfissionalResponse> create(@Valid @RequestBody ProfissionalDto.ProfissionalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.create(request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProfissionalDto.ProfissionalResponse>> findAll() {
        return ResponseEntity.ok(profissionalService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfissionalDto.ProfissionalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ProfissionalDto.ProfissionalResponse> update(@PathVariable Long id,
                                                                       @Valid @RequestBody ProfissionalDto.ProfissionalRequest request) {
        return ResponseEntity.ok(profissionalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
