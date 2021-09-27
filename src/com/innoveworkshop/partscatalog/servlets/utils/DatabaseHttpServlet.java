package com.innoveworkshop.partscatalog.servlets.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.innoveworkshop.partscatalog.config.Configuration;
import com.innoveworkshop.partscatalog.db.DatabaseConnection;

/**
 * An extension of the classic servlet class that ensures we always have a valid
 * database session that is opened and closed properly for all requests.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public abstract class DatabaseHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 677511024035059037L;
	protected DatabaseConnection db;

	/**
	 * Initializes the servlet and the database connection object.
	 */
	public DatabaseHttpServlet() {
        super();
        
		// Connect to the database and open a new session.
		db = new DatabaseConnection(Configuration.DB_MODELS_PACKAGE);
	}

	abstract protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		doGet(request, response, session);
		session.close();
	}
	
	abstract protected void doPost(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException;
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		doPost(request, response, session);
		session.close();
	}
	
	abstract protected void doDelete(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException;
	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Session session = db.openSession();
		doDelete(request, response, session);
		session.close();
	}
}
