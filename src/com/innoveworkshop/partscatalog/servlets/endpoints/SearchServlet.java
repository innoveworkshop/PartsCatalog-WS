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
import com.innoveworkshop.partscatalog.servlets.utils.ComponentSearch;
import com.innoveworkshop.partscatalog.servlets.utils.DatabaseHttpServlet;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component search servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/search")
public class SearchServlet extends DatabaseHttpServlet {
	private static final long serialVersionUID = -5685301545746031027L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		// Check if we have the required query string.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.require("query"))
			return;
		
		// Parse the search query.
		ComponentSearch search = new ComponentSearch(request.getParameter("query"));
		
		// Get the HQL query for the search.
		String hql = search.getDatabaseQueryString();
    	System.out.println(hql);
		Query query = session.createQuery(hql);
		
		// Get the results.
		@SuppressWarnings("unchecked")
		List<Component> components = (List<Component>)query.getResultList();
		
		// Setup the response formatter.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		
		// Respond to the request.
		formatter.respond("parametric", new FormattableCollection("components", components));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, Session session)
			throws ServletException, IOException {
		// We won't reply to any POST requests.
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response, Session session)
			throws ServletException, IOException {
		// We won't reply to any DELETE requests.
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
}
