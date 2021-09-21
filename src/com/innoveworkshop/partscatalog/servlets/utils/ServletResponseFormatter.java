package com.innoveworkshop.partscatalog.servlets.utils;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.innoveworkshop.partscatalog.config.Configuration;

/**
 * Allows servlets to output serialized objects in a bunch of different formats
 * automatically given a "format" query parameter.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class ServletResponseFormatter {
	private boolean verbose = false;
	private HttpServletRequest request;
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
		this.request = request;
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
		this.request = request;
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
		this.request = request;
    	this.response = response;
    	setFormat(request.getParameter("format"));
    }

    /**
     * Replies to a request with a properly formatted object.
     * 
     * @param obj Object that will be serialized and formatted.
     * 
     * @throws IOException
     * @throws ServletException
     */
    public void respond(Formattable obj) throws IOException, ServletException {
    	// Check if we should render a JSP page.
    	if (format == ServletResponseFormat.HTML) {
    		throw new ServletException("Cannot render HTML with respond(Formattable). " +
    				"Use respond(String, Formattable) instead.");
    	}
    	
    	// Regular API endpoint response.
    	response.setContentType(getMimeType());
    	response.getWriter().print(getFormattedResponse(obj));
    }

    /**
     * Replies to a request with a properly formatted object.
     * 
     * @param jspName Name of the JSP file that will be used for HTML rendering.
     * @param obj     Object that will be serialized and formatted.
     * 
     * @throws IOException
     * @throws ServletException
     */
    public void respond(String jspName, Formattable obj) throws IOException, ServletException {
    	// Check if we should render a JSP page.
    	if (format == ServletResponseFormat.HTML) {
    		request.setAttribute("object", obj);
    		request.getRequestDispatcher(Configuration.TEMPLATE_JSP_DIR +
    				jspName + ".jsp").forward(request,response);
    		
    		return;
    	}
    	
    	// Regular API endpoint response.
    	response.setContentType(getMimeType());
    	response.getWriter().print(getFormattedResponse(obj));
    }
    
    /**
     * Gets the formatted response an arbitrary format that's different from the
     * one associated with this object.
     * 
     * @param  obj    Object to be serialized and formatted.
     * @param  format Desired output format. (Note: HTML is not supported)
     * @return        Formatted output string.
     */
    public String getFormattedResponse(Formattable obj, ServletResponseFormat format) {
    	switch (format) {
    	case TEXT:
    		return obj.toPlainText(verbose);
    	case JSON:
    		return obj.toJSON(verbose).toString();
		case CSV:
			return obj.toCSV(verbose);
		case XML:
			try {
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				StringWriter writer = new StringWriter();
				transformer.transform(new DOMSource(obj.toXML(verbose)),
						new StreamResult(writer));

				return writer.getBuffer().toString();
			} catch (TransformerException e) {
				e.printStackTrace();
			}

			return null;
		case HTML:
			return "<h1>Do not call getFormattedResponse() for HTML</h1>";
    	}
    	
    	return obj.toString();
    }
    
    /**
     * Gets the formatted response in the specified format.
     * 
     * @param  obj Object to be serialized and formatted.
     * @return     Formatted output string.
     */
    public String getFormattedResponse(Formattable obj) {
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
		} else if (strFormat.equalsIgnoreCase("csv")) {
			format = ServletResponseFormat.CSV;
		} else if (strFormat.equalsIgnoreCase("html")) {
			format = ServletResponseFormat.HTML;
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
		case CSV:
			return "text/csv";
		case HTML:
			return "text/html";
		default:
			return "text/html";
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
	
	/**
	 * Should the formatted output be verbose?
	 * 
	 * @return Verbosity of the formatted output.
	 */
	public boolean getVerbose() {
		return verbose;
	}
	
	/**
	 * Sets the verbosity of the formatted output.
	 * 
	 * @param verbose Should the formatted output be verbose?
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
