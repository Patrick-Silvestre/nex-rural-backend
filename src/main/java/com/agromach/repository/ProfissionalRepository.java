package com.agromach.repository;

import com.agromach.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Profissional. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}
