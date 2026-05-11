package com.agromach.repository;

import com.agromach.entity.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Postagem. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface PostagemRepository extends JpaRepository<Postagem, Long> {
}
