package com.devcortes.primefaces.service;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class HibernateUtil {

	private SessionFactory sessionFactory = buildSessionFactory();

	@PostConstruct
	private SessionFactory buildSessionFactory() {
		System.out.println(
				"================================================================ Build Session Factory ================================================================");
		return new Configuration().configure().buildSessionFactory();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
