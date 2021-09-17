package com.innoveworkshop.partscatalog.db.models;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.innoveworkshop.partscatalog.servlets.utils.Formattable;
import com.innoveworkshop.partscatalog.servlets.utils.FormattableCollection;

/**
 * A component category database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "comp_categories")
public class Category extends Formattable {
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
	private Set<SubCategory> subCategories;
	
	/**
	 * Component category empty constructor.
	 */
	public Category() {
	}
	
	/**
	 * Gets the component category ID.
	 * 
	 * @return Category ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the component category ID.
	 * 
	 * @param id Category ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the component category name.
	 * 
	 * @return Category name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the component category name.
	 * 
	 * @param name Category name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the sub-categories that are a child of this category.
	 * 
	 * @return Child sub-categories.
	 */
	public Set<SubCategory> getSubCategories() {
		return subCategories;
	}
	
	/**
	 * Sets the sub-categories that are a child of this category.
	 * 
	 * @param subCategories Child sub-categories.
	 */
	public void setSubCategories(Set<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Category name.
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
		
		// Populate sub-categories in case we actually want it.
		if (verbose)
			json.put("subcategories", new FormattableCollection("subcategories",
					subCategories).toJSONArray());
		
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
			Element root = doc.createElement("category");
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("id");
			child.setTextContent(String.valueOf(id));
			root.appendChild(child);
			child = doc.createElement("name");
			child.setTextContent(name);
			root.appendChild(child);
			
			// Populate sub-categories in case we actually want it.
			if (verbose) {
				Node node = doc.importNode(new FormattableCollection("subcategories",
						subCategories).toXML().getDocumentElement(), true);
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
				builder.setHeader("id", "name", "subcategories");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				if (!verbose) {
					csv.printRecord(id, name);
				} else {
					csv.printRecord(id, name, StringUtils.join(subCategories, "|"));
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
		buffer.append(id);
		buffer.append(" ");
		buffer.append(toString());
		buffer.append(" [ ");
		for (Iterator<SubCategory> iterator = subCategories.iterator();
				iterator.hasNext(); ) {
			buffer.append(iterator.next().toPlainText());
			
			// Only add the comma if we are in the middle of the collection.
			if (iterator.hasNext())
				buffer.append(", ");
		}
		buffer.append(" ]");
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("id");
		headers.add("name");
		
		if (verbose)
			headers.add("subcategories");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(name);
		
		if (verbose)
			columns.add(StringUtils.join(subCategories, "|"));
		
		return columns;
	}
}
