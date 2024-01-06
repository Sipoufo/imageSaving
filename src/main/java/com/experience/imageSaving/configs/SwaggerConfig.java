package com.experience.imageSaving.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ImageSaving",
                description = "ImageSaving api documentation",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        url = "/",
                        description = "Default Server URL"
                )
        }
)
public class SwaggerConfig {
}
