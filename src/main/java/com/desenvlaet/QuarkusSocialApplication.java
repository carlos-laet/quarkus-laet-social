package com.desenvlaet;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Api quarkus Social",
                version = "1.0",
                contact = @Contact(
                        name = "Carlos Laet",
                        url = "https://www.linkedin.com/in/carlos-eduardo-bastos-laet/",
                        email = "carlosedulaet@hotmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class QuarkusSocialApplication extends Application {
}
