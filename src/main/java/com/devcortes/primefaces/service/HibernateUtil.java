package com.devcortes.primefaces.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class HibernateUtil {

	private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);

	/*private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {

			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable e) {

			LOGGER.error("Initial SessionFactory creation failed." + e.getMessage());
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}*/

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
