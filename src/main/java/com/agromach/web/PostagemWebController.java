package com.agromach.web;

import com.agromach.dto.PostagemDto;
import com.agromach.entity.Usuario;
import com.agromach.service.PostagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/postagens")
@RequiredArgsConstructor
public class PostagemWebController {

    private final PostagemService postagemService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("postagens", postagemService.findAll());
        return "postagens/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("postagem", new PostagemDto.PostagemResponse(null, "", "", null, "", null));
        return "postagens/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("postagem", postagemService.findById(id));
        return "postagens/form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam String titulo,
            @RequestParam String conteudo) {

        Usuario user = currentUser();
        PostagemDto.PostagemRequest request = new PostagemDto.PostagemRequest(titulo, conteudo, user.getId());
        if (id == null) {
            postagemService.create(request);
        } else {
            postagemService.update(id, request);
        }
        return "redirect:/postagens";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postagemService.delete(id);
        return "redirect:/postagens";
    }

    private Usuario currentUser() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
