package com.agromach.web;

import com.agromach.dto.MaquinaDto;
import com.agromach.entity.StatusMaquina;
import com.agromach.entity.TipoMaquina;
import com.agromach.service.FazendaService;
import com.agromach.service.MaquinaService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/maquinas")
@RequiredArgsConstructor
public class MaquinaWebController {

    private final MaquinaService maquinaService;
    private final FazendaService fazendaService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("maquinas", maquinaService.findAll());
        return "maquinas/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("fazendas", fazendaService.findAll());
        model.addAttribute("tiposMaquina", TipoMaquina.values());
        model.addAttribute("statusMaquina", StatusMaquina.values());
        model.addAttribute("maquina", new MaquinaDto.MaquinaResponse(null, "", null, null, 0, BigDecimal.ZERO, null, ""));
        return "maquinas/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("maquina", maquinaService.findById(id));
        model.addAttribute("fazendas", fazendaService.findAll());
        model.addAttribute("tiposMaquina", TipoMaquina.values());
        model.addAttribute("statusMaquina", StatusMaquina.values());
        return "maquinas/form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam String nome,
            @RequestParam String tipo,
            @RequestParam String status,
            @RequestParam Integer ano,
            @RequestParam BigDecimal valorEstimado,
            @RequestParam Long fazendaId) {

        MaquinaDto.MaquinaRequest request = new MaquinaDto.MaquinaRequest(
                nome, TipoMaquina.valueOf(tipo), StatusMaquina.valueOf(status), ano, valorEstimado, fazendaId);
        if (id == null) {
            maquinaService.create(request);
        } else {
            maquinaService.update(id, request);
        }
        return "redirect:/maquinas";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        maquinaService.delete(id);
        return "redirect:/maquinas";
    }
}
