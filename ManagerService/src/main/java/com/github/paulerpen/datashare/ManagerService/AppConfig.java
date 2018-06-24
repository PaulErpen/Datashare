package com.github.paulerpen.datashare.ManagerService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.springframework.cloud.client.discovery.DiscoveryClient;

@EnableWebMvc
@Configuration
public class AppConfig {
	
	@Autowired
	private Environment env;

	@Bean
	public CommonsMultipartResolver filterMultipartResolver(){
	    return new CommonsMultipartResolver();
	}
	
	@Bean
	public MongoClient mongoclient() {
		MongoClient mongoClient = new MongoClient();
		return mongoClient;
	}
}
