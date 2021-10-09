package com.innoveworkshop.partscatalog.db.models;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * A component package database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "Packages")
public class CaseStyle extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PackageID")
	private int id;
	
	@Column(name = "Name")
	private String name;

	@OneToOne(mappedBy = "caseStyle", fetch = FetchType.LAZY)
	private Image image;
	
	/**
	 * Component package empty constructor.
	 */
	public CaseStyle() {
	}
	
	/**
	 * Gets the package category ID.
	 * 
	 * @return Package ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the package category ID.
	 * 
	 * @param id Package ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the package category name.
	 * 
	 * @return Package name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the package category name.
	 * 
	 * @param name Package name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the package image.
	 * 
	 * @return Package image.
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Sets the package image.
	 * 
	 * @param image Package image.
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Package name.
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		
		// Populate the JSON object.
		json.put("id", id);
		json.put("name", name);
		
		// Populate image in case we actually want it.
		if (verbose) {
			if (image != null)
				json.put("image", image.toJSON(false));
		}
		
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
			Element root = doc.createElement("package");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("name");
			child.setTextContent(name);
			root.appendChild(child);
			
			// Populate properties and image in case we actually want it.
			Node node;
			if (verbose) {
				if (image != null) {
					node = doc.importNode(image.toXML(false).getDocumentElement(), true);
					root.appendChild(node);
				}
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
			builder.setHeader("id", "name");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				csv.printRecord(id, name);
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
		// Simply return the name if we don't need it to be verbose.
		if (!verbose)
			return toString();
		
		// Build a more complete text version.
		StringBuilder buffer = new StringBuilder();
		buffer.append("ID: " + id + System.lineSeparator());
		buffer.append("Name: " + name + System.lineSeparator());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Name");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(name);
		
		return columns;
	}
}
