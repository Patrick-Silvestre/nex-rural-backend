package com.agromach.repository;

import com.agromach.entity.Funcionario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Funcionario. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findByFazendaId(Long fazendaId);

    long countByFazendaId(Long fazendaId);
}
