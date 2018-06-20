package com.github.paulerpen.datashare.ManagerService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class AppConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public CommonsMultipartResolver filterMultipartResolver(){
	    return new CommonsMultipartResolver();
	}
}
