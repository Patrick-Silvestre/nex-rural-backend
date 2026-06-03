package com.agromach.repository;

import com.agromach.entity.Funcionario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Funcionario. Fornece operacoes de persistencia e consultas ao banco via Spring Data.
 */
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    /**
     * Lista todos os funcionarios de uma fazenda para demonstrar o CRUD 1:N.
     */
    List<Funcionario> findByFazendaId(Long fazendaId);

    /**
     * Busca um funcionario garantindo que ele pertence a fazenda informada.
     */
    Optional<Funcionario> findByIdAndFazendaId(Long id, Long fazendaId);
}
