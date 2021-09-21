package com.innoveworkshop.jsp.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Some utilities to work with URLs inside JSPs.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class URLUtils {
	/**
	 * Gets the request URL with the context path.
	 * 
	 * @param  request HTTP request
	 * @return         Request URL with the context path.
	 */
	public static String getURLWithContextPath(HttpServletRequest request) {
		String port = "";
		if ((request.getServerPort() != 80) || (request.getServerPort() != 443))
			port = ":" + request.getServerPort();

		return request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
	}
}
