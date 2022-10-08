package com.api.spaceexplorer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket dockets() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.api.spaceexplorer.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Space Explorer Challenge")
                .description("Uma API Rest em que se pode cadastrar planetas e sondas para explorer os mesmos.")
                .version("1.0")
                .contact(new Contact("Raoni Lacroux", "https://github.com/LacrouxRaoni", "lacroux.raoni@yahoo.com.br"))
                .build();
        return apiInfo;
    }
}
