package life.liquide.trades.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerOpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")))
                .info(new Info().title("App API").version("snapshot"))
                .addSecurityItem(
                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }
//		.addSecurityItem(new SecurityRequirement()
//		        .addList(securitySchemeName))
//		      .components(new Components()
//		        .addSecuritySchemes(securitySchemeName, SecurityScheme)
//		        //  .name(securitySchemeName)
//		          //.type()
//		          .scheme("bearer")
//		          .bearerFormat("JWT")));


    //Add custom header param to all API operations
//	@Bean
//    public OpenApiCustomizer openApiCustomiser() {
//        return openApi -> openApi.getPaths().values().stream()
//                .flatMap(pathItem -> pathItem.readOperations().stream().filter(operation -> !operation.getOperationId().equals("authenticate"))   )
//                .forEach(operation ->
//                operation.addParametersItem(
//                		new HeaderParameter()
//                		.required(false)
//						.name("Authorization")
//						.description("Bearer Token")
//                        .$ref("#/components/parameters/Authorization")));
//
//    }
}

