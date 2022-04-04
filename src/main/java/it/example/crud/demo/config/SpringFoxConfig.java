package it.example.crud.demo.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

@Configuration
public class SpringFoxConfig {

	@Bean
	@DependsOn("requestMappingHandlerAdapter")
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
										.alternateTypeRules(getAlternateTypeRuleForLocalDate())
										.alternateTypeRules(getAlternateTypeRuleForResponseEntity())
										.select()
										.apis(RequestHandlerSelectors.basePackage("it.example.crud.demo.controller"))
										.paths(PathSelectors.any())
										.build()
										.forCodeGeneration(true)
										.directModelSubstitute(LocalDate.class, java.sql.Date.class)
										.apiInfo(apiInfo())
										.useDefaultResponseMessages(false);
	}

	public AlternateTypeRule getAlternateTypeRuleForLocalDate() {
		TypeResolver typeResolver = new TypeResolver();
		return AlternateTypeRules.newRule(
										typeResolver.resolve(Map.class, String.class, typeResolver.resolve(LocalDate.class)),
										typeResolver.resolve(Map.class, String.class, Date.class),
										Ordered.HIGHEST_PRECEDENCE
		);
	}

	public AlternateTypeRule getAlternateTypeRuleForResponseEntity() {
		TypeResolver typeResolver = new TypeResolver();
		return AlternateTypeRules.newRule(
										typeResolver.resolve(ResponseEntity.class),
										typeResolver.resolve(Void.class)
		);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("User API").description("User services").termsOfServiceUrl("").version("1.0.0")
										.contact(new Contact("", "", "m.tomai@reply.it")).build();
	}
}