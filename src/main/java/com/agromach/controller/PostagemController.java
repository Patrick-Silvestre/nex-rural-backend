package com.agromach.controller;

import com.agromach.dto.PostagemDto;
import com.agromach.service.PostagemService;
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
 * Controller REST de Postagem. Recebe requisicoes HTTP, valida dados de entrada e delega regras para a camada de servico.
 */
@RestController
@RequestMapping(ApiPaths.POSTAGENS)
@RequiredArgsConstructor
@Tag(name = "Postagens")
public class PostagemController {

    private final PostagemService postagemService;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PostagemDto.PostagemResponse> create(@Valid @RequestBody PostagemDto.PostagemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postagemService.create(request));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PostagemDto.PostagemResponse>> findAll() {
        return ResponseEntity.ok(postagemService.findAll());
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostagemDto.PostagemResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postagemService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PostagemDto.PostagemResponse> update(@PathVariable Long id,
                                                               @Valid @RequestBody PostagemDto.PostagemRequest request) {
        return ResponseEntity.ok(postagemService.update(id, request));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postagemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
