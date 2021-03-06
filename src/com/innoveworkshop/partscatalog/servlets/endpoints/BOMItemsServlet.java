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

import com.innoveworkshop.partscatalog.db.models.BOMItem;
import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.Project;
import com.innoveworkshop.partscatalog.servlets.utils.DatabaseHttpServlet;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Project BOM items servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/bom_item")
public class BOMItemsServlet extends DatabaseHttpServlet {
	private static final long serialVersionUID = -4390161405257106151L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public BOMItemsServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		
		// Check if we have the required parent project XOR ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.requireXOR("project", "id"))
			return;
		
		// List BOM items.
		Query query;
		if (request.getParameter("project") != null) {
			// Get items from a project.
			query = session.createQuery("SELECT DISTINCT bitm FROM BOMItem bitm " +
				"LEFT JOIN FETCH bitm.component LEFT JOIN FETCH bitm.parentProject " +
				"WHERE bitm.parentProject.id = :parent ORDER BY bitm.refDes");
			query.setParameter("parent", Integer.parseInt(request.getParameter("project")));
			
			formatter.setVerbose(false);
		} else {
			// Get a single BOM item.
			query = session.createQuery("SELECT DISTINCT bitm FROM BOMItem bitm " +
					"LEFT JOIN FETCH bitm.component LEFT JOIN FETCH bitm.parentProject " +
					"WHERE bitm.id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
			
			formatter.setVerbose(true);
		}
		@SuppressWarnings("unchecked")
		List<BOMItem> bomItems = (List<BOMItem>)query.getResultList();
		
		// Respond to the request.
		if (request.getParameter("id") != null) {
			// Requested only a single object.
			if (bomItems.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			formatter.respond(bomItems.get(0));
		} else {
			// Requested a list of objects.
			formatter.respond(new FormattableCollection("bom", bomItems));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		BOMItem bomItem = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			bomItem = new BOMItem();
		} else {
			// Get the object from the database.
			bomItem = (BOMItem)session.get(BOMItem.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (bomItem == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		// Check for required parameters.
		if (!paramChecker.requireAll("populate", "quantity", "value", "refdes",
				"component", "project"))
			return;
		
		// Check if the parent project exists.
		Project project = (Project)session.get(Project.class,
				Integer.parseInt(request.getParameter("project")));
		if (project == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Check if the component exists.
		Component component = (Component)session.get(Component.class,
				Integer.parseInt(request.getParameter("component")));
		if (component == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Update the object and commit changes.
		bomItem.setPopulate(Boolean.parseBoolean(request.getParameter("populate")) |
				request.getParameter("populate").equals("1"));
		bomItem.setQuantity(Integer.parseInt(request.getParameter("quantity")));
		bomItem.setValue(request.getParameter("value"));
		bomItem.setRefDes(request.getParameter("refdes"));
		bomItem.setComponent(component);
		bomItem.setParentProject(project);
		session.saveOrUpdate(bomItem);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(bomItem);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		// Check if we have the required ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.require("id"))
			return;
		
		// Get the object from the database and check if it exists.
		BOMItem item = (BOMItem)session.get(BOMItem.class,
				Integer.parseInt(request.getParameter("id")));
		if (item == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(item);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
	}
}
