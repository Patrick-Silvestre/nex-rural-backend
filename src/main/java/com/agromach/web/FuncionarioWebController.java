package com.agromach.web;

import com.agromach.dto.FuncionarioDto;
import com.agromach.service.FazendaService;
import com.agromach.service.FuncionarioService;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioWebController {

    private final FuncionarioService funcionarioService;
    private final FazendaService fazendaService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("funcionarios", funcionarioService.findAll());
        return "funcionarios/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("fazendas", fazendaService.findAll());
        model.addAttribute("funcionario", new FuncionarioDto.FuncionarioResponse(null, "", "", BigDecimal.ZERO, LocalDate.now(), null, ""));
        return "funcionarios/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("funcionario", funcionarioService.findById(id));
        model.addAttribute("fazendas", fazendaService.findAll());
        return "funcionarios/form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam String nome,
            @RequestParam String cargo,
            @RequestParam BigDecimal salario,
            @RequestParam String dataAdmissao,
            @RequestParam Long fazendaId) {

        FuncionarioDto.FuncionarioRequest request = new FuncionarioDto.FuncionarioRequest(
                nome, cargo, salario, LocalDate.parse(dataAdmissao), fazendaId);
        if (id == null) {
            funcionarioService.create(request);
        } else {
            funcionarioService.update(id, request);
        }
        return "redirect:/funcionarios";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        funcionarioService.delete(id);
        return "redirect:/funcionarios";
    }
}
