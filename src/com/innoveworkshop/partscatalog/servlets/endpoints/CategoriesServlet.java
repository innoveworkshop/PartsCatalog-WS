package com.innoveworkshop.partscatalog.servlets.endpoints;

import java.io.IOException;
import java.util.List;

import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.innoveworkshop.partscatalog.config.Configuration;
import com.innoveworkshop.partscatalog.db.DatabaseConnection;
import com.innoveworkshop.partscatalog.db.models.Category;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component categories servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/category")
public class CategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1947590770905577729L;
	private DatabaseConnection db;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public CategoriesServlet() {
        super();
        
		// Connect to the database and open a new session.
		db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();

		Query query;
		if (request.getParameter("id") == null) {
			// List all categories.
			query = session.createQuery("FROM Category");
		} else {
			// Get a single category.
			query = session.createQuery("FROM Category WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		}
		List<Category> categories = (List<Category>)query.getResultList();
		
		// Setup the response formatter.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		
		// Respond to the request.
		if (request.getParameter("id") != null) {
			// Requested only a single object.
			if (categories.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			formatter.respond(categories.get(0));
		} else {
			// Requested a list of objects.
			formatter.respond(new FormattableCollection("categories", categories));
		}
		
		session.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		Category category = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			category = new Category();
		} else {
			// Get the object from the database.
			category = (Category)session.get(Category.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (category == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Check for required parameters.
		if (!paramChecker.require("name"))
			return;
		
		// Update the object and commit changes.
		category.setName(request.getParameter("name"));
		session.saveOrUpdate(category);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(category);
		
		session.close();
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		
		// Check if we have the required ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.require("id"))
			return;
		
		// Get the category object from the database and check if it exists.
		Category category = (Category)session.get(Category.class,
				Integer.parseInt(request.getParameter("id")));
		if (category == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(category);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
		
		session.close();
	}
}
