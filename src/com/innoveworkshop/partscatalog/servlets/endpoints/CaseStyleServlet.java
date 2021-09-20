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
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component packages servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/package")
public class CaseStyleServlet extends HttpServlet {
	private static final long serialVersionUID = -3925669235622189733L;
	private DatabaseConnection db;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public CaseStyleServlet() {
        super();
        
		// Connect to the database and open a new session.
		db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		
		Query query;
		if (request.getParameter("id") == null) {
			// List all packages.
			query = session.createQuery("FROM CaseStyle");
		} else {
			// Get a single package.
			query = session.createQuery("FROM CaseStyle WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		}
		@SuppressWarnings("unchecked")
		List<CaseStyle> packages = (List<CaseStyle>)query.getResultList();
		
		// Setup the response formatter.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		
		// Respond to the request.
		if (request.getParameter("id") != null) {
			// Requested only a single object.
			if (packages.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			formatter.respond(packages.get(0));
		} else {
			// Requested a list of objects.
			formatter.respond(new FormattableCollection("packages", packages));
		}
		
		session.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		CaseStyle caseStyle = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			caseStyle = new CaseStyle();
		} else {
			// Get the object from the database.
			caseStyle = (CaseStyle)session.get(CaseStyle.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (caseStyle == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Check for required parameters.
		if (!paramChecker.require("name"))
			return;
		
		// Update the object and commit changes.
		caseStyle.setName(request.getParameter("name"));
		session.saveOrUpdate(caseStyle);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(caseStyle);
		
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
		CaseStyle caseStyle = (CaseStyle)session.get(CaseStyle.class,
				Integer.parseInt(request.getParameter("id")));
		if (caseStyle == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(caseStyle);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
		
		session.close();
	}
}
