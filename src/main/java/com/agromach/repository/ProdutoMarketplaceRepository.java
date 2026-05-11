package com.agromach.repository;

import com.agromach.entity.ProdutoMarketplace;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de ProdutoMarketplace. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface ProdutoMarketplaceRepository extends JpaRepository<ProdutoMarketplace, Long> {
}
