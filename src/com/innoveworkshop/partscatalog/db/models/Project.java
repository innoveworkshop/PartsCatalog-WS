package com.innoveworkshop.partscatalog.db.models;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;

/**
 * A project database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "Projects")
public class Project extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ProjectID")
	private int id;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "Revision")
	private String revision;
	
	@Column(name = "Description")
	private String description;
	
	@ManyToMany(mappedBy = "parentProject", fetch = FetchType.LAZY)
	private Set<BOMItem> bomItems;
	
	/**
	 * Project empty constructor.
	 */
	public Project() {
	}
	
	/**
	 * Gets the project ID.
	 * 
	 * @return Project ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the project ID.
	 * 
	 * @param id Project ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the project name.
	 * 
	 * @return Project name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the project name.
	 * 
	 * @param name Project name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the project revision.
	 * 
	 * @return Project revision.
	 */
	public String getRevision() {
		return revision;
	}
	
	/**
	 * Sets the project revision.
	 * 
	 * @param revision Project revision.
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	/**
	 * Gets the project description.
	 * 
	 * @return Project description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the project description.
	 * 
	 * @param description Project description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the list of BOM items for this project.
	 * 
	 * @return This project's BOM items.
	 */
	public Set<BOMItem> getBOMItems() {
		return bomItems;
	}
	
	/**
	 * Sets the list of BOM items for this project.
	 * 
	 * @param items BOM items.
	 */
	public void setBOMItems(Set<BOMItem> items) {
		this.bomItems = items;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Project name.
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
		json.put("revision", revision);
		json.put("description", description);
		if (verbose)
			json.put("bom", new FormattableCollection("bom",
					bomItems).toJSONArray());
		
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
			Element root = doc.createElement("project");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("name");
			child.setTextContent(name);
			root.appendChild(child);
			child = doc.createElement("revision");
			child.setTextContent(revision);
			root.appendChild(child);
			child = doc.createElement("description");
			child.setTextContent(description);
			root.appendChild(child);
			
			// Populate components in case we actually want it.
			if (verbose) {
				Node node = doc.importNode(new FormattableCollection("bom",
						bomItems).toXML().getDocumentElement(), true);
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
			builder.setHeader("id", "name", "revision", "description");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				csv.printRecord(id, name, revision, description);
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
		buffer.append("Revision: " + revision + System.lineSeparator());
		buffer.append("Description: " + description + System.lineSeparator());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Name");
		headers.add("Revision");
		headers.add("Description");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(name);
		columns.add(revision);
		columns.add(description);
		
		return columns;
	}
}
