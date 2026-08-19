package com.agromach.repository;

import com.agromach.entity.AreaProducao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de AreaProducao. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface AreaProducaoRepository extends JpaRepository<AreaProducao, Long> {

    List<AreaProducao> findByFazendaId(Long fazendaId);
}
