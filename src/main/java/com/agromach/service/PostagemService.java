package com.agromach.service;

import com.agromach.dto.PostagemDto;
import com.agromach.entity.Postagem;
import com.agromach.entity.Usuario;
import com.agromach.exception.ResourceNotFoundException;
import com.agromach.repository.PostagemRepository;
import com.agromach.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico de negocio de Postagem. Aplica regras funcionais, validacoes e orquestra acesso a repositorios.
 */
@Service
@RequiredArgsConstructor
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Cria um novo registro no contexto da aplicacao, aplicando validacoes de negocio.
     */
    @Transactional
    public PostagemDto.PostagemResponse create(PostagemDto.PostagemRequest request) {
        Usuario autor = findUsuarioById(request.autorId());

        Postagem postagem = Postagem.builder()
            .titulo(request.titulo())
            .conteudo(request.conteudo())
            .autor(autor)
            .dataCriacao(LocalDateTime.now())
            .build();

        return toResponse(postagemRepository.save(postagem));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public List<PostagemDto.PostagemResponse> findAll() {
        return postagemRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    @Transactional(readOnly = true)
    public PostagemDto.PostagemResponse findById(Long id) {
        Postagem postagem = findEntityById(id);
        return toResponse(postagem);
    }

    /**
     * Atualiza os dados existentes de acordo com as regras de negocio da aplicacao.
     */
    @Transactional
    public PostagemDto.PostagemResponse update(Long id, PostagemDto.PostagemRequest request) {
        Postagem postagem = findEntityById(id);
        Usuario autor = findUsuarioById(request.autorId());

        postagem.setTitulo(request.titulo());
        postagem.setConteudo(request.conteudo());
        postagem.setAutor(autor);

        return toResponse(postagemRepository.save(postagem));
    }

    /**
     * Remove o registro alvo de forma controlada, respeitando regras e restricoes.
     */
    @Transactional
    public void delete(Long id) {
        Postagem postagem = findEntityById(id);
        postagemRepository.delete(postagem);
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Postagem findEntityById(Long id) {
        return postagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Postagem nao encontrada com ID " + id));
    }

    /**
     * Retorna os dados solicitados conforme os criterios da operacao.
     */
    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com ID " + id));
    }

    /**
     * Converte ou extrai dados entre formatos/estruturas usadas no sistema.
     */
    private PostagemDto.PostagemResponse toResponse(Postagem postagem) {
        return new PostagemDto.PostagemResponse(
            postagem.getId(),
            postagem.getTitulo(),
            postagem.getConteudo(),
            postagem.getAutor().getId(),
            postagem.getAutor().getNome(),
            postagem.getDataCriacao()
        );
    }
}
