package com.agromach.web;

import com.agromach.service.FazendaService;
import com.agromach.service.FuncionarioService;
import com.agromach.service.MaquinaService;
import com.agromach.service.PedidoService;
import com.agromach.service.PostagemService;
import com.agromach.service.ProdutoMarketplaceService;
import com.agromach.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DashboardController {

    private final FazendaService fazendaService;
    private final MaquinaService maquinaService;
    private final FuncionarioService funcionarioService;
    private final ProdutoMarketplaceService produtoService;
    private final PostagemService postagemService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalFazendas", fazendaService.findAll().size());
        model.addAttribute("totalMaquinas", maquinaService.findAll().size());
        model.addAttribute("totalFuncionarios", funcionarioService.findAll().size());
        model.addAttribute("totalProdutos", produtoService.findAll().size());
        model.addAttribute("totalPostagens", postagemService.findAll().size());
        model.addAttribute("totalPedidos", pedidoService.findAll().size());
        model.addAttribute("totalUsuarios", usuarioService.findAll().size());
        return "dashboard";
    }
}
