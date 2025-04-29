package com.example.demo;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//"Configuración de Swagger para la documentación de la API. Permite la visualización interactiva de los endpoints de la API."


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Login y Registro")
                        .version("1.0")
                        .description("API REST para el sistema de Login y Registro de usuarios"));
    }
}
