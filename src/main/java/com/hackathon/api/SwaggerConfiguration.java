package com.hackathon.api;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by 424047 on 4/6/2019.
 */
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket demoApi() {
        return new Docket(DocumentationType.SWAGGER_2)//<3>
                .select()//<4>
                .apis(RequestHandlerSelectors.any())//<5>
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }
}
