package com.agromach.repository;

import com.agromach.entity.Aviso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Aviso. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface AvisoRepository extends JpaRepository<Aviso, Long> {

    List<Aviso> findByFazendaId(Long fazendaId);

    List<Aviso> findByFazendaIdAndConcluidoFalseOrderByDataPrevistaAsc(Long fazendaId);
}
