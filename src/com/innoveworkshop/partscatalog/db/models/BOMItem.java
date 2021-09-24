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
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.innoveworkshop.partscatalog.servlets.utils.Formattable;

/**
 * A BOM item database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "bom_components")
public class BOMItem extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "quantity")
	private int quantity;
	
	@Column(name = "refdes")
	private String refDes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id", nullable = false)
	private Component component;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project parentProject;
	
	/**
	 * BOM item empty constructor.
	 */
	public BOMItem() {
	}
	
	/**
	 * Gets the BOM item ID.
	 * 
	 * @return BOM item ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the BOM item ID.
	 * 
	 * @param id BOM item ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the BOM item quantity.
	 * 
	 * @return BOM item quantity.
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Sets the BOM item quantity.
	 * 
	 * @param id BOM item quantity.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Gets the BOM item reference designators array as a string of
	 * space-separated values.
	 * 
	 * @return Reference designators array as a string of space-separated values.
	 */
	public String getRefDes() {
		return refDes;
	}
	
	/**
	 * Sets the space-separated reference designators string.
	 * 
	 * @param refDes Space-separated reference designators.
	 */
	public void setRefDes(String refDes) {
		this.refDes = refDes;
	}
	
	/**
	 * Sets the space-separated reference designators string using an array.
	 * 
	 * @param refDes Reference designators.
	 */
	public void setRefDes(String[] refDes) {
		setRefDes(String.join(" ", refDes));
	}
	
	/**
	 * Gets the reference designators as an array.
	 * 
	 * @return Reference designators.
	 */
	public String[] getRefDesArray() {
		return refDes.split(" ");
	}
	
	/**
	 * Gets the component of this BOM item.
	 * 
	 * @return Component of this BOM item.
	 */
	public Component getComponent() {
		return component;
	}
	
	/**
	 * Sets the component of this BOM item.
	 * 
	 * @param component Component of this BOM item.
	 */
	public void setComponent(Component component) {
		this.component = component;
	}
	
	/**
	 * Gets the project this BOM item belongs to.
	 * 
	 * @return Project of this BOM item.
	 */
	public Project getParentProject() {
		return parentProject;
	}
	
	/**
	 * Sets the project this BOM item belongs to.
	 * 
	 * @param project Project of this BOM item.
	 */
	public void setParentProject(Project project) {
		this.parentProject = project;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Reference designators string.
	 */
	@Override
	public String toString() {
		return refDes;
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		
		// Populate the JSON object.
		json.put("id", id);
		json.put("quantity", quantity);
		json.put("refdes", new JSONArray(getRefDesArray()));
		json.put("component", component.toJSON());
		
		// Populate sub-categories in case we actually want it.
		if (verbose)
			json.put("parent", parentProject.toJSON());
		
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
			Element root = doc.createElement("item");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("quantity");
			child.setTextContent(String.valueOf(quantity));
			root.appendChild(child);
			Node node = doc.importNode(component.toXML(false).getDocumentElement(), true);
			root.appendChild(node);
			child = doc.createElement("refdesignators");
			for (String ref : getRefDesArray()) {
				Element item = doc.createElement("refdes");
				item.setTextContent(ref);
				child.appendChild(item);
			}
			root.appendChild(child);
			
			// Populate sub-categories in case we actually want it.
			if (verbose) {
				node = doc.importNode(parentProject.toXML(false).getDocumentElement(), true);
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
			builder.setHeader("id", "refdes", "component");
			if (verbose)
				builder.setHeader("id", "refdes", "component", "project");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				if (!verbose) {
					csv.printRecord(id, refDes, component);
				} else {
					csv.printRecord(id, refDes, component, parentProject);
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
		buffer.append("Quantity: " + quantity + System.lineSeparator());
		buffer.append("Component: " + component + System.lineSeparator());
		buffer.append("RefDes: " + refDes + System.lineSeparator());
		buffer.append("Project: " + parentProject + System.lineSeparator());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Qnt");
		headers.add("RefDes");
		headers.add("Component");
		
		if (verbose)
			headers.add("Project");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(String.valueOf(quantity));
		columns.add(refDes);
		columns.add(component.toString());
		
		if (verbose)
			columns.add(parentProject.toString());
		
		return columns;
	}
}
