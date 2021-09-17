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
import com.innoveworkshop.partscatalog.db.models.ComponentPackage;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component packages servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/package")
public class ComponentPackageServlet extends HttpServlet {
	private static final long serialVersionUID = -3925669235622189733L;
	private DatabaseConnection db;
	private Session session;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ComponentPackageServlet() {
        super();
        
		// Connect to the database and open a new session.
		db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
		session = db.openSession();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Query query;
		if (request.getParameter("id") == null) {
			// List all packages.
			query = session.createQuery("FROM ComponentPackage");
		} else {
			// Get a single package.
			query = session.createQuery("FROM ComponentPackage WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		}
		@SuppressWarnings("unchecked")
		List<ComponentPackage> packages = (List<ComponentPackage>)query.getResultList();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableCollection("packages", packages));
	}
}
