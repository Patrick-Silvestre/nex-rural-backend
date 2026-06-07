package com.agromach.web;

import com.agromach.dto.PedidoDto;
import com.agromach.entity.StatusPedido;
import com.agromach.entity.Usuario;
import com.agromach.service.PedidoService;
import com.agromach.service.ProdutoMarketplaceService;
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
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoWebController {

    private final PedidoService pedidoService;
    private final ProdutoMarketplaceService produtoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pedidos", pedidoService.findAll());
        return "pedidos/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("produtos", produtoService.findAll());
        model.addAttribute("statusList", StatusPedido.values());
        model.addAttribute("pedido", new PedidoDto.PedidoResponse(null, null, "", null, "", StatusPedido.PENDENTE));
        return "pedidos/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.findById(id));
        model.addAttribute("produtos", produtoService.findAll());
        model.addAttribute("statusList", StatusPedido.values());
        return "pedidos/form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam Long produtoId,
            @RequestParam String status) {

        Usuario user = currentUser();
        Long compradorId = id == null ? user.getId() : pedidoService.findById(id).compradorId();
        PedidoDto.PedidoRequest request = new PedidoDto.PedidoRequest(compradorId, produtoId, StatusPedido.valueOf(status));
        if (id == null) {
            pedidoService.create(request);
        } else {
            pedidoService.update(id, request);
        }
        return "redirect:/pedidos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        pedidoService.delete(id);
        return "redirect:/pedidos";
    }

    private Usuario currentUser() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
