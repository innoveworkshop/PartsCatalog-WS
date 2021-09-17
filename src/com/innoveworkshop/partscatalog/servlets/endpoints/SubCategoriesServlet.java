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
import com.innoveworkshop.partscatalog.db.models.SubCategory;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component sub-categories servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/subcategory")
public class SubCategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = -3925669235622189733L;
	private DatabaseConnection db;
	private Session session;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SubCategoriesServlet() {
        super();
        
		// Connect to the database and open a new session.
		db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
		session = db.openSession();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check if we have the required parent category XOR ID parameter.
		if (!((request.getParameter("parent") != null) ^ (request.getParameter("id") != null))) {
			response.sendError(422, "Unprocessable Entity");
			return;
		}
		
		// List sub-categories.
		Query query;
		if (request.getParameter("parent") != null) {
			// Get sub-categories from a category.
			query = session.createQuery("FROM SubCategory WHERE parentCategory.id = :parent");
			query.setParameter("parent", Integer.parseInt(request.getParameter("parent")));
		} else {
			// Get a single sub-category.
			query = session.createQuery("FROM SubCategory WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		}
		@SuppressWarnings("unchecked")
		List<SubCategory> subCategories = (List<SubCategory>)query.getResultList();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableCollection("subcategories", subCategories));
	}
}
