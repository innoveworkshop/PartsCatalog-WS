package com.innoveworkshop.partscatalog.servlets.endpoints;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.hibernate.Session;

import com.innoveworkshop.partscatalog.db.models.CaseStyle;
import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.Image;
import com.innoveworkshop.partscatalog.servlets.utils.DatabaseHttpServlet;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableMessage;
import com.innoveworkshop.partscatalog.servlets.utils.ServletParameterChecker;
import com.innoveworkshop.partscatalog.servlets.utils.ServletResponseFormatter;

/**
 * Component image servlet handler.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@WebServlet("/image")
@MultipartConfig
public class ImagesServlet extends DatabaseHttpServlet {
	private static final long serialVersionUID = 3454758453402757114L;

	/**
     * @see DatabaseHttpServlet#HttpServlet()
     */
    public ImagesServlet() {
    	super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		
		// Check if we want a specific image or a component/package image.
		Query query;
		if (request.getParameter("id") != null) {
			// Get image via its ID.
			query = session.createQuery("FROM Image WHERE id = :id");
			query.setParameter("id", Integer.parseInt(request.getParameter("id")));
		} else {
			// Get a component or package image.
			if (!paramChecker.requireOnlyOne("component", "package"))
				return;
			
			if (request.getParameter("component") != null) {
				// Component image.
				query = session.createQuery("FROM Image WHERE component.id = :component");
				query.setParameter("component", Integer.parseInt(request.getParameter("component")));
			} else {
				// Package image.
				query = session.createQuery("FROM Image WHERE caseStyle.id = :package");
				query.setParameter("package", Integer.parseInt(request.getParameter("package")));
			}
		}
		@SuppressWarnings("unchecked")
		List<Image> images = (List<Image>)query.getResultList();
		
		// Did we actually find any images?
		if (images.isEmpty()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Do we want to download the image or just get data about it?
		if (request.getParameter("download") != null) {
			// Get the actual image.
			OutputStream out = response.getOutputStream();
			String mimeType = "image/" + request.getParameter("format");
			
			// Handle the special JPG case.
			if (request.getParameter("format").equalsIgnoreCase("jpg"))
				mimeType = "image/jpeg";
			
			// Check if the format is actually valid.
			if (!ImageIO.getImageWritersByMIMEType(mimeType).hasNext()) {
				response.sendError(422, "Unprocessable Entity");
				return;
			}
			
			// Respond with the image in the desired format.
			response.setContentType(mimeType);
			ImageIO.write(images.get(0).getBufferedImage(), request.getParameter("format"), out);
			out.close();
		} else {
			// Get data.
			ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
			formatter.setVerbose(true);
			formatter.respond(images.get(0));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		Image image = null;

		// Check if we are editing or adding.
		if (request.getParameter("id") == null) {
			// Create a brand new one.
			image = new Image();
		} else {
			// Get the object from the database.
			image = (Image)session.get(Image.class,
					Integer.parseInt(request.getParameter("id")));
			
			// Check if it exists.
			if (image == null) {
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
		
		// Make sure we can only upload a component or package image.
		if (!paramChecker.requireXOR("component", "package"))
			return;
		
		// Populate the component or package property.
		if (request.getParameter("component") != null) {
			// Component
			Component component = (Component)session.get(Component.class,
					Integer.parseInt(request.getParameter("component")));
			if (component == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			image.setComponent(component);
			image.setCaseStyle(null);
		} else {
			// Package
			CaseStyle caseStyle = (CaseStyle)session.get(CaseStyle.class,
					Integer.parseInt(request.getParameter("package")));
			if (caseStyle == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			image.setCaseStyle(caseStyle);
			image.setComponent(null);
		}
		
		// Set the image file data and commit changes.
		BufferedImage bufImage = ImageIO.read(filePart.getInputStream());
		image.setImage(bufImage);
		session.saveOrUpdate(image);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(image);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response,
			Session session) throws ServletException, IOException {
		// Check if we have the required ID parameter.
		ServletParameterChecker paramChecker = new ServletParameterChecker(request, response);
		if (!paramChecker.require("id"))
			return;
		
		// Get the object from the database and check if it exists.
		Image image = (Image)session.get(Image.class,
				Integer.parseInt(request.getParameter("id")));
		if (image == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the entry and commit the changes.
		session.delete(image);
		session.getTransaction().commit();
		
		// Setup the response formatter and respond to the request.
		ServletResponseFormatter formatter = new ServletResponseFormatter(request, response);
		formatter.setVerbose(true);
		formatter.respond(new FormattableMessage("Deleted successfully"));
	}
}
