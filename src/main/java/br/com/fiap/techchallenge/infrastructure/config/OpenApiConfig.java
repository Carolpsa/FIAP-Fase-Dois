package br.com.fiap.techchallenge.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("Tech Challenge - Gestão de Restaurantes")
                .description("API da Fase 2: tipos de usuário, usuários, restaurantes e itens do cardápio")
                .version("1.0.0"));
    }
}
