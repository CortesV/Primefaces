package com.devcortes.primefaces.standalone;

import java.util.HashMap;

import javax.faces.webapp.FacesServlet;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.devcortes.primefaces.scope.ViewScope;
import com.sun.faces.config.ConfigureListener;

@Configuration
@SpringBootApplication
@ComponentScan({ "com.devcortes.primefaces" })
public class PrimefacesApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PrimefacesApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PrimefacesApplication.class);
	}

	@Bean
	public static ViewScope viewScope() {
		return new ViewScope();
	}

	/**
	 * Allows the use of @Scope("view") on Spring @Component, @Service
	 * and @Controller beans
	 */
	@Bean
	public static CustomScopeConfigurer scopeConfigurer() {
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("view", viewScope());
		configurer.setScopes(hashMap);
		return configurer;
	}

	/*
	 * Below sets up the Faces Servlet for Spring Boot
	 */
	@Bean
	public FacesServlet facesServlet() {
		return new FacesServlet();
	}

	@Bean
	public ServletRegistrationBean facesServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(facesServlet(), "*.xhtml");
		registration.setName("FacesServlet");
		return registration;
	}

	@Bean
	public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
		return new ServletListenerRegistrationBean<ConfigureListener>(new ConfigureListener());
	}
}
