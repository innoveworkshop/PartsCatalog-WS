package com.innoveworkshop.partscatalog.db;

import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

/**
 * A helper class to work with the database connection.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class DatabaseConnection {
	private SessionFactory factory;
	private Configuration config;
	
	/**
	 * Initializes the database session factory.
	 * 
	 * @param modelsPackages Package where all of the annotated class models for
	 *                       the database entities are located.
	 */
	public DatabaseConnection(String modelsPackage) {
		// Setup the configuration and search for annotated class models.
		config = new Configuration().configure();
		Reflections reflections = new Reflections(modelsPackage, new TypeAnnotationsScanner());
		Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Entity.class, true);
		for (Class<?> cls : annotatedClasses) {
			config.addAnnotatedClass(cls);
		}
		
		// Build the session factory.
		factory = config.buildSessionFactory();
	}

	/**
	 * Opens a new session with the database.
	 * 
	 * @param  beginTransaction Should we automatically begin a transaction in
	 *                          this session?
	 * @return                  Database session to operate on.
	 */
	public Session openSession(boolean beginTransaction) {
		// Open a new session.
		Session session = factory.openSession();
		
		// Should we also begin a transaction?
		if (beginTransaction)
			session.beginTransaction();
		
		return session;
	}
	
	/**
	 * Opens a new database session and begins a transaction.
	 * 
	 * @return Database session to operate on.
	 */
	public Session openSession() {
		return openSession(true);
	}
	
	/**
	 * Closes the database connection in case there is one.
	 */
	public void close() {
		// Close the database connection.
		if (factory != null)
			factory.close();
	}
}
