package com.agromach.repository;

import com.agromach.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Pedido. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
