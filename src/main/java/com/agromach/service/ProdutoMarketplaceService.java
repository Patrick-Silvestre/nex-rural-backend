package com.agromach.service;

import com.agromach.dto.ProdutoMarketplaceDto;
import com.agromach.entity.ProdutoMarketplace;
import com.agromach.entity.Usuario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.ProdutoMarketplaceRepository;
import com.agromach.repository.UsuarioRepository;
import com.agromach.security.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de ProdutoMarketplace. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class ProdutoMarketplaceService {

    private final ProdutoMarketplaceRepository produtoMarketplaceRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public ProdutoMarketplaceDto.ProdutoMarketplaceResponse create(ProdutoMarketplaceDto.ProdutoMarketplaceRequest request) {
        SecurityUtils.requireOwnerOrAdmin(request.vendedorId());
        Usuario vendedor = findUsuarioById(request.vendedorId());

        ProdutoMarketplace produto = ProdutoMarketplace.builder()
            .titulo(request.titulo())
            .descricao(request.descricao())
            .categoria(request.categoria())
            .preco(request.preco())
            .vendedor(vendedor)
            .build();

        return toResponse(produtoMarketplaceRepository.save(produto));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public List<ProdutoMarketplaceDto.ProdutoMarketplaceResponse> findAll() {
        return produtoMarketplaceRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public ProdutoMarketplaceDto.ProdutoMarketplaceResponse findById(Long id) {
        ProdutoMarketplace produto = findEntityById(id);
        return toResponse(produto);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public ProdutoMarketplaceDto.ProdutoMarketplaceResponse update(Long id, ProdutoMarketplaceDto.ProdutoMarketplaceRequest request) {
        ProdutoMarketplace produto = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(produto.getVendedor().getId());
        SecurityUtils.requireOwnerOrAdmin(request.vendedorId());
        Usuario vendedor = findUsuarioById(request.vendedorId());

        produto.setTitulo(request.titulo());
        produto.setDescricao(request.descricao());
        produto.setCategoria(request.categoria());
        produto.setPreco(request.preco());
        produto.setVendedor(vendedor);

        return toResponse(produtoMarketplaceRepository.save(produto));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @Transactional
    public void delete(Long id) {
        ProdutoMarketplace produto = findEntityById(id);
        SecurityUtils.requireOwnerOrAdmin(produto.getVendedor().getId());
        produtoMarketplaceRepository.delete(produto);
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private ProdutoMarketplace findEntityById(Long id) {
        return produtoMarketplaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com ID " + id));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com ID " + id));
    }

    /**
     * Converte ou extrai dados entre formatos/estruturas usadas no sistema.
     */
    private ProdutoMarketplaceDto.ProdutoMarketplaceResponse toResponse(ProdutoMarketplace produto) {
        return new ProdutoMarketplaceDto.ProdutoMarketplaceResponse(
            produto.getId(),
            produto.getTitulo(),
            produto.getDescricao(),
            produto.getCategoria(),
            produto.getPreco(),
            produto.getVendedor().getId(),
            produto.getVendedor().getNome()
        );
    }
}
