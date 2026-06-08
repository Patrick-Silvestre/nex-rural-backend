package com.agromach.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Disponibiliza atributos comuns para as telas Thymeleaf.
 */
@ControllerAdvice(assignableTypes = {
    DashboardController.class,
    FazendaWebController.class,
    FuncionarioWebController.class,
    MaquinaWebController.class,
    PedidoWebController.class,
    PostagemWebController.class,
    ProdutoWebController.class,
    WebAuthController.class
})
public class WebModelAdvice {

    /**
     * Mantem o caminho atual disponivel para destacar o item ativo no menu lateral.
     */
    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
