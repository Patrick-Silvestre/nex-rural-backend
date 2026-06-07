package com.agromach.web;

import com.agromach.dto.ProdutoMarketplaceDto;
import com.agromach.entity.CategoriaProduto;
import com.agromach.entity.Usuario;
import com.agromach.service.ProdutoMarketplaceService;
import java.math.BigDecimal;
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
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoWebController {

    private final ProdutoMarketplaceService produtoService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("produtos", produtoService.findAll());
        return "produtos/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("categorias", CategoriaProduto.values());
        model.addAttribute("produto", new ProdutoMarketplaceDto.ProdutoMarketplaceResponse(null, "", "", null, BigDecimal.ZERO, null, ""));
        return "produtos/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoService.findById(id));
        model.addAttribute("categorias", CategoriaProduto.values());
        return "produtos/form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam String titulo,
            @RequestParam String descricao,
            @RequestParam String categoria,
            @RequestParam BigDecimal preco) {

        Usuario user = currentUser();
        ProdutoMarketplaceDto.ProdutoMarketplaceRequest request = new ProdutoMarketplaceDto.ProdutoMarketplaceRequest(
                titulo, descricao, CategoriaProduto.valueOf(categoria), preco, user.getId());
        if (id == null) {
            produtoService.create(request);
        } else {
            produtoService.update(id, request);
        }
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        produtoService.delete(id);
        return "redirect:/produtos";
    }

    private Usuario currentUser() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
