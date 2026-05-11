package com.agromach.service;

import com.agromach.dto.PedidoDto;
import com.agromach.entity.Pedido;
import com.agromach.entity.ProdutoMarketplace;
import com.agromach.entity.StatusPedido;
import com.agromach.entity.Usuario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.PedidoRepository;
import com.agromach.repository.ProdutoMarketplaceRepository;
import com.agromach.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Pedido. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoMarketplaceRepository produtoMarketplaceRepository;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public PedidoDto.PedidoResponse create(PedidoDto.PedidoRequest request) {
        Usuario comprador = findUsuarioById(request.compradorId());
        ProdutoMarketplace produto = findProdutoById(request.produtoId());

        Pedido pedido = Pedido.builder()
            .comprador(comprador)
            .produto(produto)
            .status(request.status() != null ? request.status() : StatusPedido.PENDENTE)
            .build();

        return toResponse(pedidoRepository.save(pedido));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public List<PedidoDto.PedidoResponse> findAll() {
        return pedidoRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public PedidoDto.PedidoResponse findById(Long id) {
        Pedido pedido = findEntityById(id);
        return toResponse(pedido);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public PedidoDto.PedidoResponse update(Long id, PedidoDto.PedidoRequest request) {
        Pedido pedido = findEntityById(id);
        Usuario comprador = findUsuarioById(request.compradorId());
        ProdutoMarketplace produto = findProdutoById(request.produtoId());

        pedido.setComprador(comprador);
        pedido.setProduto(produto);
        pedido.setStatus(request.status() != null ? request.status() : pedido.getStatus());

        return toResponse(pedidoRepository.save(pedido));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @Transactional
    public void delete(Long id) {
        Pedido pedido = findEntityById(id);
        pedidoRepository.delete(pedido);
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Pedido findEntityById(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido nao encontrado com ID " + id));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com ID " + id));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private ProdutoMarketplace findProdutoById(Long id) {
        return produtoMarketplaceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com ID " + id));
    }

    /**
     * Converte ou extrai dados entre formatos/estruturas usadas no sistema.
     */
    private PedidoDto.PedidoResponse toResponse(Pedido pedido) {
        return new PedidoDto.PedidoResponse(
            pedido.getId(),
            pedido.getComprador().getId(),
            pedido.getComprador().getNome(),
            pedido.getProduto().getId(),
            pedido.getProduto().getTitulo(),
            pedido.getStatus()
        );
    }
}
