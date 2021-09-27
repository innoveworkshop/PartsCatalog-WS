package com.innoveworkshop.partscatalog.servlets.endpoints;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.hibernate.Session;

import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.Datasheet;
import com.innoveworkshop.partscatalog.servlets.utils.DatabaseHttpServlet;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component datasheet servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/datasheet")
@MultipartConfig
public class DatasheetsServlet extends DatabaseHttpServlet {
	private static final long serialVersionUID = -5817596820506109817L;

	/**
     * @see DatabaseHttpServlet#HttpServlet()
     */
    public DatasheetsServlet() {
    	super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		
		// Check if we want a specific datasheet ID or a component datasheet.
		Query query;
		if (request.getParameter("id") != null) {
			// Get datasheet via its ID.
			query = session.createQuery("FROM Datasheet WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		} else {
			// Get a component datasheet.
			if (!paramChecker.require("component"))
				return;
			
			query = session.createQuery("FROM Datasheet WHERE component.id = :component");
			query.setParameter("component", Integer.parseInt(request.getParameter("component")));
		}
		@SuppressWarnings("unchecked")
		List<Datasheet> datasheets = (List<Datasheet>)query.getResultList();
		
		// Did we actually find any datasheets?
		if (datasheets.isEmpty()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Do we want to download the datasheet or just get data about it?
		if (request.getParameter("download") != null) {
			// Get the actual image.
			OutputStream out = response.getOutputStream();

			// Respond with the datasheet file.
			response.setContentType("application/pdf");
			response.setContentLength(datasheets.get(0).getFile().length);
			out.write(datasheets.get(0).getFile(), 0, datasheets.get(0).getFile().length);
			out.close();
		} else {
			// Get data.
			ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
			formatter.setVerbose(true);
			formatter.respond(datasheets.get(0));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		Datasheet datasheet = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			datasheet = new Datasheet();
		} else {
			// Get the object from the database.
			datasheet = (Datasheet)session.get(Datasheet.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (datasheet == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Check for the required file parameter.
		Part filePart = request.getPart("file");
		if (filePart == null) {
			response.sendError(422, "Unprocessable Entity");
			return;
		}
		
		// Make sure we have the required parameters.
		if (!paramChecker.require("component"))
			return;
		
		// Check if the component actually exists.
		Component component = (Component)session.get(Component.class,
				Integer.parseInt(request.getParameter("component")));
		if (component == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Set the component, datasheet file data, and commit changes.
		datasheet.setComponent(component);
		datasheet.setFile(filePart.getInputStream().readAllBytes());
		session.saveOrUpdate(datasheet);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(datasheet);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		// Check if we have the required ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.require("id"))
			return;
		
		// Get the object from the database and check if it exists.
		Datasheet datasheet = (Datasheet)session.get(Datasheet.class,
				Integer.parseInt(request.getParameter("id")));
		if (datasheet == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(datasheet);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
	}
}
