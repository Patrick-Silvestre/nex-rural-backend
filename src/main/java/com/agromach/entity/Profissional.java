package com.agromach.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade JPA Profissional. Diretorio de veterinarios, agronomos, fornecedores de insumo/semente
 * e de gado (com ou sem rastreabilidade) e trabalhadores de campo que atendem a propriedade.
 * Nao ha checkout/pedido aqui - e um diretorio de contato, nao um marketplace transacional.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profissionais")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProfissional tipo;

    @Column(nullable = false)
    private String telefone;

    private String descricao;

    @Builder.Default
    private boolean rastreabilidade = false;

    private String cidadeRegiao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cadastrado_por_id", nullable = false)
    private Usuario cadastradoPor;
}
