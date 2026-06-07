package com.agromach.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebAuthController {

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) Boolean error,
            @RequestParam(required = false) Boolean logout,
            Model model) {
        if (Boolean.TRUE.equals(error)) model.addAttribute("error", "Email ou senha incorretos.");
        if (Boolean.TRUE.equals(logout)) model.addAttribute("msg", "Logout realizado com sucesso.");
        return "auth/login";
    }
}
