package de.maxpru.orderhub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI orderHubOpenAPI() {
        String securityScheme = "basicAuth";
        Info openApiInfo = new Info()
                .title("OrderHub API")
                .version("v1")
                .description("REST API for managing products and orders. "
                        + "Products can be managed by administrators, orders are created by the authenticated user. "
                        + "All /api/** endpoints are protected via HTTP Basic authentication, while the Swagger UI is publicly accessible.");

        return new OpenAPI()
                .info(openApiInfo)
                .addSecurityItem(new SecurityRequirement().addList(securityScheme))
                .components(new Components()
                        .addSecuritySchemes(
                                securityScheme,
                                new SecurityScheme()
                                        .name(securityScheme)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }
}
