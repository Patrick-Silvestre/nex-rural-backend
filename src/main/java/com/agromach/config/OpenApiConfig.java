package com.agromach.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
/**
 * Configuracao do OpenAPI/Swagger. Centraliza metadados da API e padrao de autenticacao documentado.
 */
public class OpenApiConfig {

    /**
     * Executa a responsabilidade principal desta operacao no fluxo da aplicacao.
     */
    @Bean
    public OpenAPI agroMachOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("AgroMach API")
                .description("Backend da plataforma AgroMach")
                .version("v1"))
            .components(new Components().addSecuritySchemes(
                "bearerAuth",
                new io.swagger.v3.oas.models.security.SecurityScheme()
                    .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
