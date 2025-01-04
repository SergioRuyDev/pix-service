package com.sergioruy.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "Quarkus 3.0", description = "Project for Pix Operations")
        },
        info = @Info(
                title = "Quarkus 3.0",
                version = "1.0.0",
                contact = @Contact(
                        name = "Sergio Ruy",
                        url = "www.sergioruy.com",
                        email = "sergioruyenator@gmail.com"
                ),
                license = @License(
                        name = "com.sergioruy",
                        url = "www.sergioruy.com"))
)
public class OpenApiConfig extends Application {
}
