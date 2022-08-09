package com.example.template.be.config;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.template.be.util.LoggerHelper;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${rest.prefix}")
	private String endpointPrefix;

	private String controllerPackage = "com.example.template.be.controller";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Bean
	public Docket getDocket() {
		LoggerHelper.log(logger, String.format("Swagger will be configured for endpoint prefix: %s", endpointPrefix));

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage(controllerPackage)).paths(PathSelectors.ant(endpointPrefix))
				.build();
	}

	private ApiInfo getApiInfo() {
		return new ApiInfo("Template BE", "Template BE for project external", "1.0.0", null, null, null, null,
				new Vector<>());
	}
}
