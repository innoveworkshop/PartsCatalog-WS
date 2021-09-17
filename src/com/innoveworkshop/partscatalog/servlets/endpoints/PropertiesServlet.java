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
import com.innoveworkshop.partscatalog.db.models.Property;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component properties list servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/property")
public class PropertiesServlet extends HttpServlet {
	private static final long serialVersionUID = -3925669235622189733L;
	private DatabaseConnection db;
	private Session session;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public PropertiesServlet() {
        super();
        
		// Connect to the database and open a new session.
		db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
		session = db.openSession();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check if we have the required parent component XOR ID parameter.
		if (!((request.getParameter("component") != null) ^ (request.getParameter("id") != null))) {
			response.sendError(422, "Unprocessable Entity");
			return;
		}
		
		// List properties.
		Query query;
		if (request.getParameter("component") != null) {
			// Get properties from a component.
			query = session.createQuery("FROM Property WHERE component.id = :component");
			query.setParameter("component", Integer.parseInt(request.getParameter("component")));
		} else {
			// Get a single property.
			query = session.createQuery("FROM Property WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		}
		@SuppressWarnings("unchecked")
		List<Property> properties = (List<Property>)query.getResultList();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableCollection("properties", properties));
	}
}
