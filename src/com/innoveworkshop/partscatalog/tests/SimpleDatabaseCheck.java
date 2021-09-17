package com.innoveworkshop.partscatalog.tests;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.innoveworkshop.partscatalog.config.Configuration;
import com.innoveworkshop.partscatalog.db.DatabaseConnection;
import com.innoveworkshop.partscatalog.db.models.Category;
import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.CaseStyle;
import com.innoveworkshop.partscatalog.db.models.Property;
import com.innoveworkshop.partscatalog.db.models.SubCategory;

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
			// Connect to the database and open a new session.
			DatabaseConnection db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
			Session session = db.openSession();
			
			// List categories.
			System.out.println("==== Categories ====");
			@SuppressWarnings("unchecked")
			List<Category> categories = (List<Category>)session.createQuery("FROM Category").list();
			for (Category category : categories) {
				System.out.println(category.getName());
				
				// List sub-categories.
				Set<SubCategory> subCategories = category.getSubCategories();
				for (SubCategory subCategory : subCategories) {
					System.out.println("    " + subCategory.getName());
				}
			}
			System.out.println();
			
			// List categories.
			System.out.println("==== Packages ====");
			@SuppressWarnings("unchecked")
			List<CaseStyle> packages = (List<CaseStyle>)session.createQuery("FROM ComponentPackage").list();
			for (CaseStyle compPackage : packages) {
				System.out.println(compPackage.getName());
			}
			System.out.println();
			
			// Get a single component.
			System.out.println("==== Component ====");
			@SuppressWarnings("unchecked")
			List<Component> components = (List<Component>)session.createQuery("FROM Component WHERE id = 1").list();
			for (Component component : components) {
				System.out.println("ID: " + component.getID());
				System.out.println("Name: " + component.getName());
				System.out.println("Quantity: " + component.getQuantity());
				System.out.println("Description: " + component.getDescription());
				System.out.println("Category: " + component.getCategory());
				System.out.println("Sub-Category: " + component.getSubCategory());
				System.out.println("Package: " + component.getPackage());
				System.out.println("Properties:");
				
				Set<Property> properties = component.getProperties();
				for (Property property : properties) {
					System.out.println("    " + property);
				}
				
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
