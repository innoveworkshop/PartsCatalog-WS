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

import com.innoveworkshop.partscatalog.db.models.CaseStyle;
import com.innoveworkshop.partscatalog.db.models.Category;
import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.SubCategory;
import com.innoveworkshop.partscatalog.servlets.utils.DatabaseHttpServlet;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormat;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/component")
public class ComponentsServlet extends DatabaseHttpServlet {
	private static final long serialVersionUID = -2373637008679770019L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ComponentsServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		// Check if we have the required parent category XOR ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.requireOnlyOneOrNone("id", "category", "subcategory", "package"))
			return;
		
		Query query;
		if (request.getParameter("id") != null) {
			// Get a single component.
			query = session.createQuery("FROM Component WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		} else if (request.getParameter("category") != null) {
			// List components from a category.
			query = session.createQuery("FROM Component WHERE category.id = :category");
			query.setParameter("category", Integer.parseInt(request.getParameter("category")));
		} else if (request.getParameter("subcategory") != null) {
			// List components from a sub-category.
			query = session.createQuery("FROM Component WHERE subCategory.id = :subcategory");
			query.setParameter("subcategory", Integer.parseInt(request.getParameter("subcategory")));
		} else if (request.getParameter("package") != null) {
			// List components from a package.
			query = session.createQuery("FROM Component WHERE caseStyle.id = :package");
			query.setParameter("package", Integer.parseInt(request.getParameter("package")));
		} else {
			// List all components.
			query = session.createQuery("FROM Component");
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
			if (formatter.getFormat() == ServletResponseFormat.HTML) {
				// Send some additional objects to render the parametric search.
				@SuppressWarnings("unchecked")
				List<Category> categories = (List<Category>)session.createQuery("FROM Category").list();
				@SuppressWarnings("unchecked")
				List<SubCategory> subCategories = (List<SubCategory>)session.createQuery("FROM SubCategory").list();
				@SuppressWarnings("unchecked")
				List<CaseStyle> caseStyle = (List<CaseStyle>)session.createQuery("FROM CaseStyle").list();
				
				request.setAttribute("categories", categories);
				request.setAttribute("subcategories", subCategories);
				request.setAttribute("casestyles", caseStyle);
			}
			
			formatter.respond("parametric", new FormattableCollection("components", components));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
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
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
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
	}
}
