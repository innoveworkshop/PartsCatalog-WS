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

import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.Property;
import com.innoveworkshop.partscatalog.servlets.utils.DatabaseHttpServlet;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component properties list servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/property")
public class PropertiesServlet extends DatabaseHttpServlet {
	private static final long serialVersionUID = 5353104629852682490L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public PropertiesServlet() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		// Check if we have the required parent component XOR ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.requireXOR("component", "id"))
			return;
		
		// List properties.
		Query query;
		if (request.getParameter("component") != null) {
			// Get properties from a component.
			query = session.createQuery("SELECT DISTINCT prop FROM Property prop " +
				"LEFT JOIN FETCH prop.component WHERE prop.component.id = :component " +
				"ORDER BY prop.name");
			query.setParameter("component", Integer.parseInt(request.getParameter("component")));
		} else {
			// Get a single property.
			query = session.createQuery("SELECT DISTINCT prop FROM Property prop " +
				"LEFT JOIN FETCH prop.component WHERE prop.id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		}
		@SuppressWarnings("unchecked")
		List<Property> properties = (List<Property>)query.getResultList();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		
		// Respond to the request.
		if (request.getParameter("id") != null) {
			// Requested only a single object.
			if (properties.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			formatter.respond(properties.get(0));
		} else {
			// Requested a list of objects.
			formatter.respond(new FormattableCollection("properties", properties));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		Property property = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			property = new Property();
		} else {
			// Get the object from the database.
			property = (Property)session.get(Property.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (property == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Check for required parameters.
		if (!paramChecker.requireAll("name", "value", "component"))
			return;
		
		// Check if the parent component exists.
		Component component = (Component)session.get(Component.class,
				Integer.parseInt(request.getParameter("component")));
		if (component == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Update the object and commit changes.
		property.setName(request.getParameter("name"));
		property.setValue(request.getParameter("value"));
		property.setComponent(component);
		session.saveOrUpdate(property);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(property);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		
		// Check if we have the required ID parameter.
		if (!paramChecker.require("id"))
			return;
		
		// Get the object from the database and check if it exists.
		Property property = (Property)session.get(Property.class,
				Integer.parseInt(request.getParameter("id")));
		if (property == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(property);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
	}
}
