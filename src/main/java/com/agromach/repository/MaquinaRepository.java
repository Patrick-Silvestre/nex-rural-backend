package com.agromach.repository;

import com.agromach.entity.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Maquina. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
}
