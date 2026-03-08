package com.company.projecte_esport.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;




/**
 *
 * @author Jesus
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "API Projecte Esport", version = "1.0", description = "Documentació de la API per a la gestió de reserves esportives"),
        security = @SecurityRequirement(name = "bearerAuth") // Aplica el candado a toda la API
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
