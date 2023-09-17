package org.example.com.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Configuration class for OpenAPI documentation settings. It defines the basic information about the API,
 * including its title, description, version, and contact details. Additionally, it specifies the security scheme
 * for basic authentication.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Bank application api",
                description = "Bank application", version = "1.0.0",
                contact = @Contact(
                        name = "Natalie Werch"
                )
        )
)
@SecurityScheme(
        name = "basicauth",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class OpenApiConfig {
    // Configuration settings for OpenAPI documentation.
}
