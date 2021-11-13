package com.gitofolio.api.configuration;

import org.springframework.util.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration{
	
	@Bean
	public Docket api(){
		return new Docket(DocumentationType.OAS_30)
			.useDefaultResponseMessages(true)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.gitofolio.api.controller.user"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(this.apiInfo());
	}
	
	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
                .title("gitofolio user api")
                .description("user api swagger configuration")
                .version("1.0")
                .build();
	}
	
}