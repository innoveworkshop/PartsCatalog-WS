package com.innoveworkshop.partscatalog.tests;

import java.util.List;

import org.hibernate.Session;

import com.innoveworkshop.partscatalog.db.DatabaseConnection;
import com.innoveworkshop.partscatalog.db.models.Category;

/**
 * A simple command-line application to check the database and print some data.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class SimpleDatabaseCheck {
	/**
	 * Program's main entry point.
	 * 
	 * @param args Command-line arguments passed to the program.
	 */
	public static void main(String[] args) {
		
		System.out.println("PartsCatalog - Simple Database Check");
		System.out.println();
		
		try {
			DatabaseConnection db = new DatabaseConnection("com.innoveworkshop.partscatalog.db.models");

			// Open a new session.
			Session session = db.openSession();
			
			// List categories.
			@SuppressWarnings("unchecked")
			List<Category> categories = (List<Category>)session.createQuery("FROM Category").list();
			for (Category category : categories) {
				System.out.println(category.getName());
			}
			
			// Clean up.
			session.getTransaction().commit();
			session.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
