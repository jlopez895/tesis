package ar.edu.iua;


import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		// @formatter:off
	    return new Docket(DocumentationType.SWAGGER_2)  
	  	      .select()                                  
	  	      .apis(RequestHandlerSelectors.any())   
	  	      //.paths(PathSelectors.any())     
	  	      .paths(PathSelectors.ant("/api/final/**"))
	  	      .build()
	  	      .apiInfo(apiInfoMetadata());
		// @formatter:on

	}
	@SuppressWarnings("unchecked")
	private ApiInfo apiInfoMetadata() {
		// @formatter:off
		@SuppressWarnings("rawtypes")
		ApiInfo apiInfo = new ApiInfo(
	    	"API REST Examen Parcial Ing Web 3",
	        "Ingeniería Web 3, IUA",
	        "1.0",          
	        "Términos del servicio",
	        new Contact("Benitez, Lopez, Perez", "https://github.com/Ems737", "@iua.edu.ar"),
	        "Apache License Version 2.0",
	        "https://www.apache.org/licenses/LICENSE-2.0", new ArrayList());
	     // @formatter:on
		return apiInfo;
	}



}
