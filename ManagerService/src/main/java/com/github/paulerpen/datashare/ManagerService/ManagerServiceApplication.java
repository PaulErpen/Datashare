package com.github.paulerpen.datashare.ManagerService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.paulerpen.datashare.ManagerService.User.UserRepository;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class ManagerServiceApplication {
	@Autowired
	private UserRepository repository;
	
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(ManagerServiceApplication.class, args);
    }
    /*
     * Gives out all users in the database on startup.
     */
    @PostConstruct
    public void setup() {
		// fetch all user
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		try {
			for (com.github.paulerpen.datashare.ManagerService.User.User user : repository.findAll()) {
				System.out.println(user.toString());
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
				String result = encoder.encode(user.getPassword());
				System.out.println(result);
			}
		}catch(Exception e) {
			System.out.println("No Database found. Commencing operation. Not all features will be provided.");
		}
    }
}
