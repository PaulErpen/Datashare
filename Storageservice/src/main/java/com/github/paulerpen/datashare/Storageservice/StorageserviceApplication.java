package com.github.paulerpen.datashare.Storageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class StorageserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageserviceApplication.class, args);
	}
}
