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
import com.innoveworkshop.partscatalog.db.models.CaseStyle;
import com.innoveworkshop.partscatalog.db.models.Category;
import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.SubCategory;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/component")
public class ComponentsServlet extends HttpServlet {
	private static final long serialVersionUID = -3925669235622189733L;
	private DatabaseConnection db;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ComponentsServlet() {
        super();
        
		// Connect to the database.
		db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		
		Query query;
		if (request.getParameter("id") == null) {
			// List all components.
			query = session.createQuery("FROM Component");
		} else {
			// Get a single component.
			query = session.createQuery("FROM Component WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		}
		@SuppressWarnings("unchecked")
		List<Component> components = (List<Component>)query.getResultList();
		
		// Setup the response formatter.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		
		// Respond to the request.
		if (request.getParameter("id") != null) {
			// Requested only a single object.
			if (components.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			formatter.respond(components.get(0));
		} else {
			// Requested a list of objects.
			formatter.respond(new FormattableCollection("components", components));
		}
		
		session.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		Component component = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			component = new Component();
		} else {
			// Get the object from the database.
			component = (Component)session.get(Component.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (component == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Check for required parameters.
		if (!paramChecker.requireAll("name", "quantity", "description", "category",
				"subcategory", "package"))
			return;
		
		// Check if the category exists.
		Category category = (Category)session.get(Category.class,
				Integer.parseInt(request.getParameter("category")));
		if (category == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Check if the sub-category exists.
		SubCategory subCategory = (SubCategory)session.get(SubCategory.class,
				Integer.parseInt(request.getParameter("subcategory")));
		if (subCategory == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Check if the package exists.
		CaseStyle caseStyle = (CaseStyle)session.get(CaseStyle.class,
				Integer.parseInt(request.getParameter("package")));
		if (caseStyle == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Update the object and commit changes.
		component.setName(request.getParameter("name"));
		component.setQuantity(Integer.parseInt(request.getParameter("quantity")));
		component.setDescription(request.getParameter("description"));
		component.setCategory(category);
		component.setSubCategory(subCategory);
		component.setCaseStyle(caseStyle);
		session.saveOrUpdate(component);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(component);
		
		session.close();
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		
		// Check if we have the required ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.require("id"))
			return;
		
		// Get the object from the database and check if it exists.
		Component component = (Component)session.get(Component.class,
				Integer.parseInt(request.getParameter("id")));
		if (component == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(component);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
		session.close();
	}
}
