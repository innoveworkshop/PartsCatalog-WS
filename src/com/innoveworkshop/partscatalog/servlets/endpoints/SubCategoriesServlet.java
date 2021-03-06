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

import com.innoveworkshop.partscatalog.db.models.Category;
import com.innoveworkshop.partscatalog.db.models.SubCategory;
import com.innoveworkshop.partscatalog.servlets.utils.DatabaseHttpServlet;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component sub-categories servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/subcategory")
public class SubCategoriesServlet extends DatabaseHttpServlet {
	private static final long serialVersionUID = 8959639095357662911L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SubCategoriesServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		
		// Check if we have the required parent category XOR ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.requireXOR("category", "id"))
			return;
		
		// List sub-categories.
		Query query;
		if (request.getParameter("category") != null) {
			// Get sub-categories from a category.
			query = session.createQuery("SELECT DISTINCT subcat FROM SubCategory subcat " +
				"LEFT JOIN FETCH subcat.parentCategory WHERE subcat.parentCategory.id = :parent " +
				"ORDER BY subcat.name");
			query.setParameter("parent", Integer.parseInt(request.getParameter("category")));
			
			formatter.setVerbose(false);
		} else {
			// Get a single sub-category.
			query = session.createQuery("SELECT DISTINCT subcat FROM SubCategory subcat " +
				"LEFT JOIN FETCH subcat.parentCategory WHERE subcat.id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
			
			formatter.setVerbose(true);
		}
		@SuppressWarnings("unchecked")
		List<SubCategory> subCategories = (List<SubCategory>)query.getResultList();
		
		// Respond to the request.
		if (request.getParameter("id") != null) {
			// Requested only a single object.
			if (subCategories.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			formatter.respond(subCategories.get(0));
		} else {
			// Requested a list of objects.
			formatter.respond(new FormattableCollection("subcategories", subCategories));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		SubCategory subCategory = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			subCategory = new SubCategory();
		} else {
			// Get the object from the database.
			subCategory = (SubCategory)session.get(SubCategory.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (subCategory == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Check for required parameters.
		if (!paramChecker.requireAll("name", "category"))
			return;
		
		// Check if the parent category exists.
		Category category = (Category)session.get(Category.class,
				Integer.parseInt(request.getParameter("category")));
		if (category == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Update the object and commit changes.
		subCategory.setName(request.getParameter("name"));
		subCategory.setParentCategory(category);
		session.saveOrUpdate(subCategory);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(subCategory);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		// Check if we have the required ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.require("id"))
			return;
		
		// Get the object from the database and check if it exists.
		SubCategory subCategory = (SubCategory)session.get(SubCategory.class,
				Integer.parseInt(request.getParameter("id")));
		if (subCategory == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(subCategory);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
	}
}
