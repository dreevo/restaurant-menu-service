package com.restaurant.tastyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TastyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TastyServiceApplication.class, args);
	}

}
