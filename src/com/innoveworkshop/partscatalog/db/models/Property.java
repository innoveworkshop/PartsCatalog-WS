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
import javax.persistence.ManyToOne;
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
 * A component property database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "comp_properties")
public class Property extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "value")
	private String value;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id", nullable = false)
	private Component component;
	
	/**
	 * Component property empty constructor.
	 */
	public Property() {
	}
	
	/**
	 * Gets the component property ID.
	 * 
	 * @return Component property ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the component property ID.
	 * 
	 * @param id Component property ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the component property name.
	 * 
	 * @return Component property name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the component property name.
	 * 
	 * @param name Component property name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the component property value.
	 * 
	 * @return Component property value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the component property value.
	 * 
	 * @param value Component property value.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the parent component that this property belongs to.
	 * 
	 * @return Parent component.
	 */
	public Component getComponent() {
		return component;
	}
	
	/**
	 * Sets the parent component that this property belongs to.
	 * 
	 * @param component Parent component.
	 */
	public void setComponent(Component component) {
		this.component = component;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Component property name and value separated by a colon.
	 */
	@Override
	public String toString() {
		return name + ": " + value;
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		
		// Populate the JSON object.
		json.put("id", id);
		json.put("name", name);
		json.put("value", value);
		if (verbose)
			json.put("parent", component.toJSON());
		
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
			Element root = doc.createElement("property");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("name");
			child.setTextContent(name);
			root.appendChild(child);
			child = doc.createElement("value");
			child.setTextContent(value);
			root.appendChild(child);
			if (verbose) {
				Node node = doc.importNode(component.toXML().getDocumentElement(), true);
				root.appendChild(node);
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
			builder.setHeader("id", "name", "value");
			if (verbose)
				builder.setHeader("id", "name", "value", "parent");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				if (!verbose) {
					csv.printRecord(id, name, value);
				} else {
					csv.printRecord(id, name, value, component);
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
		// Simply return the name if we don't need it to be verbose.
		if (!verbose)
			return toString();
		
		// Build a more complete text version.
		StringBuilder buffer = new StringBuilder();
		buffer.append("ID: " + id + System.lineSeparator());
		buffer.append("Name: " + name + System.lineSeparator());
		buffer.append("Value: " + value + System.lineSeparator());
		buffer.append("Component: " + component + System.lineSeparator());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Name");
		headers.add("Value");
		headers.add("Parent Component");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(name);
		columns.add(value);
		columns.add(component.getName());
		
		return columns;
	}
}
