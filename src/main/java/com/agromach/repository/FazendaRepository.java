package com.agromach.repository;

import com.agromach.entity.Fazenda;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Fazenda. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface FazendaRepository extends JpaRepository<Fazenda, Long> {

    List<Fazenda> findByProprietarioId(Long proprietarioId);
}
