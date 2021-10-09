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
 * A component sub-category database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "SubCategories")
public class SubCategory extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SubCategoryID")
	private int id;
	
	@Column(name = "Name")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ParentID", nullable = false)
	private Category parentCategory;
	
	/**
	 * Component sub-category empty constructor.
	 */
	public SubCategory() {
	}
	
	/**
	 * Gets the sub-category category ID.
	 * 
	 * @return Sub-category ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the sub-category category ID.
	 * 
	 * @param id Sub-category ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the sub-category category name.
	 * 
	 * @return Sub-category name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the sub-category category name.
	 * 
	 * @param name Sub-category name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the parent category this belongs to.
	 * 
	 * @return Parent category.
	 */
	public Category getParentCategory() {
		return parentCategory;
	}
	
	/**
	 * Sets the parent category this belongs to.
	 * 
	 * @param parent Parent category.
	 */
	public void setParentCategory(Category parent) {
		this.parentCategory = parent;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Sub-category name.
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		
		// Populate JSON object.
		json.put("id", id);
		json.put("name", name);
		if (verbose)
			json.put("parent", parentCategory.toJSON());
		
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
			Element root = doc.createElement("subcategory");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("name");
			child.setTextContent(name);
			root.appendChild(child);
			if (verbose) {
				Node node = doc.importNode(parentCategory.toXML().getDocumentElement(), true);
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
			builder.setHeader("id", "name");
			if (verbose)
				builder.setHeader("id", "name", "parent");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				if (!verbose) {
					csv.printRecords(id, name);
				} else {
					csv.printRecords(id, name, parentCategory.getName());
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
		buffer.append("Category: " + parentCategory + System.lineSeparator());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Name");
		
		if (verbose)
			headers.add("Parent Category");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(name);
		
		if (verbose)
			columns.add(parentCategory.getName());
		
		return columns;
	}
}
