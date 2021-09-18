package com.innoveworkshop.partscatalog.servlets.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple class to help servlets check for required, optional, and conditional
 * parameters.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class ServletParameterChecker {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	/**
	 * Initializes the parameter checker to do its job.
	 * 
	 * @param request  Servlet request object.
	 * @param response Servlet response object.
	 */
	public ServletParameterChecker(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	/**
	 * Checks if a parameter specified is set.
	 * WARNING: This function will send a HTTP error automatically.
	 * 
	 * @param  parameter Parameter to be checked.
	 * @return           True if the parameter is present.
	 * 
	 * @throws IOException If something goes wrong when replying to the client.
	 */
	public boolean require(String parameter) throws IOException {
		if (request.getParameter(parameter) == null) {
			response.sendError(422, "Unprocessable Entity");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if all of the parameters specified are set.
	 * WARNING: This function will send a HTTP error automatically.
	 * 
	 * @param  parameters Parameters to be checked.
	 * @return            True if all of the parameters are present.
	 * 
	 * @throws IOException If something goes wrong when replying to the client.
	 */
	public boolean requireAll(String... parameters) throws IOException {
		// Go through the parameters checking for their existence.
		for (String parameter : parameters) {
			if (!require(parameter))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if only one of the two parameters is set.
	 * WARNING: This function will send a HTTP error automatically.
	 * 
	 * @param  param1 First parameter to be checked.
	 * @param  param2 Second parameter to be checked.
	 * @return        True if only one of the parameters is set.
	 * 
	 * @throws IOException If something goes wrong when replying to the client.
	 */
	public boolean requireXOR(String param1, String param2) throws IOException {
		if (!((request.getParameter(param1) != null) ^ (request.getParameter(param2) != null))) {
			response.sendError(422, "Unprocessable Entity");
			return false;
		}
		
		return true;
	}
}
