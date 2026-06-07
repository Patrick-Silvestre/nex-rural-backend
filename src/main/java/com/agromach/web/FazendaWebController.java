package com.agromach.web;

import com.agromach.dto.FazendaDto;
import com.agromach.entity.Usuario;
import com.agromach.service.FazendaService;
import com.agromach.service.UsuarioService;
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
@RequestMapping("/fazendas")
@RequiredArgsConstructor
public class FazendaWebController {

    private final FazendaService fazendaService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("fazendas", fazendaService.findAll());
        return "fazendas/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("fazenda", new FazendaDto.FazendaResponse(null, "", "", 0.0, "", null, ""));
        return "fazendas/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("fazenda", fazendaService.findById(id));
        model.addAttribute("usuarios", usuarioService.findAll());
        return "fazendas/form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam String nome,
            @RequestParam String localizacao,
            @RequestParam Double tamanhoHectares,
            @RequestParam String tipoProducao,
            @RequestParam Long proprietarioId) {

        FazendaDto.FazendaRequest request = new FazendaDto.FazendaRequest(nome, localizacao, tamanhoHectares, tipoProducao, proprietarioId);
        if (id == null) {
            fazendaService.create(request);
        } else {
            fazendaService.update(id, request);
        }
        return "redirect:/fazendas";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        fazendaService.delete(id);
        return "redirect:/fazendas";
    }

    private Usuario currentUser() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
