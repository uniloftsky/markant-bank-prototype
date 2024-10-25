package net.uniloftsky.markant.bank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Swagger OpenAPI
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI configureOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API for Banking Application")
                        .description("API to manage banking accounts and perform the common transactions.<br>" +
                                "This API supports the following endpoints:<br>" +
                                "- to check the account balance<br>" +
                                "- deposit money to account<br>" +
                                "- withdraw money from account<br>" +
                                "- transfer money from one account to another<br>" +
                                "- check history of transactions<br>"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenApiCustomizer disableDefaultResponseMessagesCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation ->
                        operation.setResponses(new ApiResponses())
                )
        );
    }


}
