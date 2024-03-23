package com.ygstar.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(crapi())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ygstar.backend.sys.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo crapi(){
        return new ApiInfoBuilder()
                .title("springboot后端")
                .description("ygstar组的后端工程")
                .version("1.0")
                .build();
    }
}

