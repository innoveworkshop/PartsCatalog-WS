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
 * A component datasheet database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "Datasheets")
public class Datasheet extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DatasheetID")
	private int id;
	
	@Lob
	@Column(name = "FileContent", nullable = false, columnDefinition = "VARBINARY")
	private byte[] file;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ComponentID", nullable = false)
	private Component component;
	
	/**
	 * Component datasheet empty constructor.
	 */
	public Datasheet() {
	}
	
	/**
	 * Gets the component datasheet ID.
	 * 
	 * @return Component datasheet ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the component datasheet ID.
	 * 
	 * @param id Component datasheet ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the component datasheet file.
	 * 
	 * @return Component datasheet file bytes.
	 */
	public byte[] getFile() {
		return file;
	}
	
	/**
	 * Sets the component datasheet file.
	 * 
	 * @param file Component datasheet file bytes.
	 */
	public void setFile(byte[] file) {
		this.file = file;
	}
	
	/**
	 * Gets the component that this datasheet belongs to.
	 * 
	 * @return Component this datasheet belongs to.
	 */
	public Component getComponent() {
		return component;
	}
	
	/**
	 * Sets the component that this datasheet belongs to.
	 * 
	 * @param component Component this datasheet belongs to.
	 */
	public void setComponent(Component component) {
		this.component = component;
	}
	
	@Override
	public String toString() {
		return "Datasheet #" + id;
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		
		// Populate the JSON object.
		json.put("id", id);
		
		// Append the referred object.
		if (verbose)
			json.put("component", (component != null) ? component.toJSON(false) : JSONObject.NULL);
		
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
			Element root = doc.createElement("datasheet");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Append the image contents.
			if (verbose) {
				if (component != null) {
					Node node = doc.importNode(component.toXML(false).getDocumentElement(), true);
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
			builder.setHeader("id", "component");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				csv.printRecord(id, component);
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
		buffer.append("Component: " + component + System.lineSeparator());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		
		if (verbose)
			headers.add("Component");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		
		if (verbose)
			columns.add(String.valueOf(component));
		
		return columns;
	}
}
