package com.devcortes.primefaces.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@SpringBootApplication
public class PrimefacesApplication{

	public static void main(String[] args) {
		SpringApplication.run(PrimefacesApplication.class, args);
	}

}
