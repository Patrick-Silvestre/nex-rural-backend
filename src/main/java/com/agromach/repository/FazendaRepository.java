package com.agromach.repository;

import com.agromach.entity.Fazenda;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Fazenda. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface FazendaRepository extends JpaRepository<Fazenda, Long> {

    /**
     * Verifica se um usuario ja possui fazenda vinculada no relacionamento 1:1.
     */
    boolean existsByProprietarioId(Long proprietarioId);

    /**
     * Verifica se outro registro ja usa o proprietario informado no relacionamento 1:1.
     */
    boolean existsByProprietarioIdAndIdNot(Long proprietarioId, Long id);

    /**
     * Busca a fazenda vinculada ao usuario no relacionamento 1:1.
     */
    Optional<Fazenda> findByProprietarioId(Long proprietarioId);
}
