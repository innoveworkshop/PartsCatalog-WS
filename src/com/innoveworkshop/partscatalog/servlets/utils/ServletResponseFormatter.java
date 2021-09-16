package com.innoveworkshop.partscatalog.servlets.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class to be inherited by other servlets that allows for a dynamic output
 * format given by a query parameter.
 */
public class ServletResponseFormatter {
	private HttpServletResponse response;
	private ServletResponseFormat format = ServletResponseFormat.JSON;
	
	/**
	 * Initializes the formatter with all of the necessary parameters.
	 * 
	 * @param request  Servlet request object.
	 * @param response Servlet response object.
	 * @param format   Desired output format.
	 */
	public ServletResponseFormatter(HttpServletRequest request,
			HttpServletResponse response, ServletResponseFormat format) {
    	this.response = response;
    	this.format = format;
    }
	
	/**
	 * Initializes the formatter with all of the necessary parameters.
	 * 
	 * @param request   Servlet request object.
	 * @param response  Servlet response object.
	 * @param strFormat Desired output format as a string.
	 */
	public ServletResponseFormatter(HttpServletRequest request,
			HttpServletResponse response, String strFormat) {
    	this.response = response;

    	setFormat(strFormat);
    }
	
	/**
	 * Initializes the formatter and tries to get the desired format from the
	 * request parameter "format".
	 * 
	 * @param request  Servlet request object.
	 * @param response Servlet response object.
	 */
    public ServletResponseFormatter(HttpServletRequest request, HttpServletResponse response) {
    	this.response = response;
    	
    	setFormat(request.getParameter("format"));
    }

    /**
     * Replies to a request with a properly formatted object.
     * 
     * @param obj Object that will be serialized and formatted.
     * @throws IOException
     */
    public void respond(Object obj) throws IOException {
    	response.setContentType(getMimeType());
    	response.getWriter().print(getFormattedResponse(obj));
    }
    
    /**
     * Gets the formatted response an arbitrary format that's different from the
     * one associated with this object.
     * 
     * @param  obj    Object to be serialized and formatted.
     * @param  format Desired output format.
     * @return        Formatted output string.
     */
    public String getFormattedResponse(Object obj, ServletResponseFormat format) {
    	return obj.toString();
    }
    
    /**
     * Gets the formatted response in the specified format.
     * 
     * @param  obj Object to be serialized and formatted.
     * @return     Formatted output string.
     */
    public String getFormattedResponse(Object obj) {
    	return getFormattedResponse(obj, format);
    }

    /**
     * Gets the currently set response format.
     * 
     * @return Response format.
     */
	public ServletResponseFormat getFormat() {
		return format;
	}
	
	/**
	 * Sets the response format.
	 * 
	 * @param format Desired response format.
	 */
	public void setFormat(ServletResponseFormat format) {
		this.format = format;
	}
	
	/**
	 * Sets the response format using a string.
	 * 
	 * @param strFormat String representation of the desired response format.
	 */
	public void setFormat(String strFormat) {
		// Ignore if the format string is null.
		if (strFormat == null)
			return;
		
		// Start testing.
		if (strFormat.equalsIgnoreCase("json")) {
			format = ServletResponseFormat.JSON;
		} else if (strFormat.equalsIgnoreCase("xml")) {
			format = ServletResponseFormat.XML;
		} else if (strFormat.equalsIgnoreCase("txt")) {
			format = ServletResponseFormat.TEXT;
		} else if (strFormat.equalsIgnoreCase("html")) {
			format = ServletResponseFormat.HTML;
		} else if (strFormat.equalsIgnoreCase("csv")) {
			format = ServletResponseFormat.CSV;
		}
	}
	
	/**
	 * Gets the MIME type string for an arbitrary format.
	 * 
	 * @param  format Format to get the MIME type string from.
	 * @return        MIME type of the format. (Note: Defaults to plain text)
	 */
	public String getMimeType(ServletResponseFormat format) {
		switch (format) {
		case JSON:
			return "application/json";
		case XML:
			return "application/xml";
		case TEXT:
			return "text/plain";
		case HTML:
			return "text/html";
		case CSV:
			return "text/csv";
		default:
			return "text/plain";
		}
	}
	
	/**
	 * Gets the MIME type string of the specified format.
	 * 
	 * @return MIME type of the format.
	 */
	public String getMimeType() {
		return getMimeType(format);
	}
}
