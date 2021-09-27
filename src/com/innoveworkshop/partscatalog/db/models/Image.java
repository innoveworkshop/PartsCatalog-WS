package com.innoveworkshop.partscatalog.db.models;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.innoveworkshop.partscatalog.servlets.utils.Formattable;

/**
 * A component image database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "comp_images")
public class Image extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Lob
	@Column(name = "image", nullable = false, columnDefinition = "mediumblob")
	private byte[] image;
	
	@Column(name = "format")
	private String format;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id", nullable = true)
	private Component component;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", nullable = true)
	private CaseStyle caseStyle;
	
	/**
	 * Component image empty constructor.
	 */
	public Image() {
	}
	
	/**
	 * Gets the component image ID.
	 * 
	 * @return Component image ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the component image ID.
	 * 
	 * @param id Component image ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the component image file.
	 * 
	 * @return Component image file bytes.
	 */
	public byte[] getImage() {
		return image;
	}
	
	/**
	 * Sets the component image file.
	 * 
	 * @param image Component image file bytes.
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	/**
	 * Gets the component image file format.
	 * 
	 * @return Component image file format.
	 */
	public String getFormat() {
		return format;
	}
	
	/**
	 * Sets the component image file format.
	 * 
	 * @param format Component image file format.
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
	/**
	 * Gets the component that this image belongs to.
	 * 
	 * @return Component this image belongs to.
	 */
	public Component getComponent() {
		return component;
	}
	
	/**
	 * Sets the component that this image belongs to.
	 * 
	 * @param component Component this image belongs to.
	 */
	public void setComponent(Component component) {
		this.component = component;
	}
	
	/**
	 * Gets the package this image represents.
	 * 
	 * @return Package this image represents.
	 */
	public CaseStyle getCaseStyle() {
		return caseStyle;
	}
	
	/**
	 * Sets the package this image represents.
	 * 
	 * @param caseStyle Package this image represents.
	 */
	public void setCaseStyle(CaseStyle caseStyle) {
		this.caseStyle = caseStyle;
	}
	
	/**
	 * Gets the image MIME type.
	 * 
	 * @return Image MIME type.
	 */
	public String getMimeType() {
		return "image/" + format.toLowerCase();
	}
	
	/**
	 * Gets the image file in Base64 form.
	 * 
	 * @return Base64 of the image file.
	 */
	public String getImageAsBase64() {
		return Base64.getEncoder().encodeToString(image);
	}
	
	/**
	 * Gets the image file in data URI form.
	 * 
	 * @return Data URI of the image.
	 */
	public String getImageAsDataURI() {
		StringBuilder buf = new StringBuilder();
		
		buf.append("data:");
		buf.append(getMimeType());
		buf.append(";base64,");
		buf.append(getImageAsBase64());
		
		return buf.toString();
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		
		// Populate the JSON object.
		json.put("id", id);
		json.put("format", format);
		json.put("component", (component != null) ? component.toJSON() : JSONObject.NULL);
		json.put("package", (caseStyle != null) ? caseStyle.toJSON() : JSONObject.NULL);
		
		// Append the image as a data URI.
		if (verbose)
			json.put("image", getImageAsDataURI());
		
		return json;
	}

	@Override
	public Document toXML(boolean verbose) {
		try {
			// Setup the XML writer.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Create the root element.
			Document doc = builder.newDocument();
			Element root = doc.createElement("image");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("format");
			child.setTextContent(format);
			root.appendChild(child);
			Node node;
			if (component != null) {
				node = doc.importNode(component.toXML(false).getDocumentElement(), true);
				root.appendChild(node);
			}
			if (caseStyle != null) {
				node = doc.importNode(caseStyle.toXML(false).getDocumentElement(), true);
				root.appendChild(node);
			}
			
			// Append the image contents.
			if (verbose) {
				child = doc.createElement("file");
				child.setAttribute("mimetype", getMimeType());
				child.setTextContent(getImageAsBase64());
				root.appendChild(child);
			}
			
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String toCSV(boolean verbose) {
		try {
			// Setup the CSV writer.
			StringWriter writer = new StringWriter();
			CSVFormat.Builder builder = CSVFormat.Builder.create(CSVFormat.DEFAULT);
			builder.setHeader("id", "format", "component", "package");
			if (verbose)
				builder.setHeader("id", "format", "component", "package", "image");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				if (!verbose) {
					csv.printRecord(id, format, component, caseStyle);
				} else {
					csv.printRecord(id, format, component, caseStyle, getImageAsBase64());
				}

				csv.flush();
			}
			
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String toPlainText(boolean verbose) {
		// Build a plain text version.
		StringBuilder buffer = new StringBuilder();
		buffer.append("ID: " + id + System.lineSeparator());
		buffer.append("Format: " + format + System.lineSeparator());
		buffer.append("Component: " + component + System.lineSeparator());
		buffer.append("Package: " + caseStyle + System.lineSeparator());
		buffer.append("Content-Type: " + getMimeType() + System.lineSeparator());
		buffer.append(System.lineSeparator());

		// Append the image.
		if (verbose)
			buffer.append(getImageAsBase64());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Format");
		headers.add("Component");
		headers.add("Package");
		
		if (verbose)
			headers.add("Image");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(format);
		columns.add(String.valueOf(component));
		columns.add(String.valueOf(caseStyle));
		
		if (verbose)
			columns.add(getImageAsDataURI());
		
		return columns;
	}
}
