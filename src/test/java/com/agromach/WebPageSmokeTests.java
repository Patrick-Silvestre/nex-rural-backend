package com.agromach;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agromach.entity.Role;
import com.agromach.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:agromach-web-test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.h2.console.enabled=false"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebPageSmokeTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginPageIsPublicAndRendersSuccessfully() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    void authenticatedThymeleafPagesRenderSuccessfully() throws Exception {
        Usuario admin = Usuario.builder()
            .id(1L)
            .nome("Administrador")
            .email("admin@agromach.com")
            .senha("admin123")
            .role(Role.ADMIN)
            .build();

        String[] paths = {
            "/",
            "/fazendas",
            "/fazendas/new",
            "/maquinas",
            "/maquinas/new",
            "/funcionarios",
            "/funcionarios/new",
            "/produtos",
            "/produtos/new",
            "/pedidos",
            "/pedidos/new",
            "/postagens",
            "/postagens/new"
        };

        for (String path : paths) {
            mockMvc.perform(get(path).with(user(admin)))
                .andExpect(status().isOk());
        }
    }
}
