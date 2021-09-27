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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
 * A component database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "components")
public class Component extends Formattable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "quantity")
	private int quantity;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcategory_id", nullable = false)
	private SubCategory subCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", nullable = false)
	private CaseStyle caseStyle;
	
	@OneToMany(mappedBy = "component", fetch = FetchType.LAZY)
	private Set<Property> properties;
	
	@OneToOne(mappedBy = "component", fetch = FetchType.LAZY)
	private Image image;
	
	/**
	 * Component category empty constructor.
	 */
	public Component() {
	}
	
	/**
	 * Gets the component ID.
	 * 
	 * @return Component ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the component ID.
	 * 
	 * @param id Component ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the component name.
	 * 
	 * @return Component name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the component name.
	 * 
	 * @param name Component name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the component quantity.
	 * 
	 * @return Component quantity.
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Sets the component quantity.
	 * 
	 * @param quantity Component quantity.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Gets the component description.
	 * 
	 * @return Component description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the component description.
	 * 
	 * @param description Component description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the component category.
	 * 
	 * @return Category this component belongs to.
	 */
	public Category getCategory() {
		return category;
	}
	
	/**
	 * Sets the category this component belongs to.
	 * 
	 * @param category Component category.
	 */
	public void setCategory(Category category) {
		this.category = category;
	}
	
	/**
	 * Gets the sub-category this component belongs to.
	 * 
	 * @return Component sub-category.
	 */
	public SubCategory getSubCategory() {
		return subCategory;
	}
	
	/**
	 * Sets the sub-category this component belongs to.
	 * 
	 * @param subcategory Component sub-category.
	 */
	public void setSubCategory(SubCategory subcategory) {
		this.subCategory = subcategory;
	}
	
	/**
	 * Gets the component package.
	 * 
	 * @return Component package.
	 */
	public CaseStyle getCaseStyle() {
		return caseStyle;
	}
	
	/**
	 * Sets the component package.
	 * 
	 * @param caseStyle Component package.
	 */
	public void setCaseStyle(CaseStyle caseStyle) {
		this.caseStyle = caseStyle;
	}
	
	/**
	 * Gets the component package.
	 * 
	 * @return Component package.
	 * @see    #getCaseStyle()
	 */
	public CaseStyle getPackage() {
		return getCaseStyle();
	}
	
	/**
	 * Sets the component package.
	 * @see #setCaseStyle(CaseStyle)
	 * 
	 * @param caseStyle Component package.
	 */
	public void setPackage(CaseStyle caseStyle) {
		setCaseStyle(caseStyle);
	}
	
	/**
	 * Gets the associated component properties.
	 * 
	 * @return Component properties.
	 */
	public Set<Property> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the associated component properties.
	 * 
	 * @param properties Component properties.
	 */
	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}
	
	/**
	 * Gets the component image.
	 * 
	 * @return Component image.
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Sets the component image.
	 * 
	 * @param image Component image.
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Component name.
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
		json.put("quantity", quantity);
		json.put("description", description);
		json.put("category", category.toJSON(false));
		json.put("subcategory", subCategory.toJSON(false));
		json.put("package", caseStyle != null ? caseStyle.toJSON(false) :
			JSONObject.NULL);
		
		// Populate properties in case we actually want it.
		if (verbose) {
			json.put("properties", new FormattableCollection("properties",
					properties).toJSONArray());
			if (image != null)
				json.put("image", image.toJSON(true));
		} else {
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
			Element root = doc.createElement("component");
			root.setAttribute("id", String.valueOf(id));
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("name");
			child.setTextContent(name);
			root.appendChild(child);
			child = doc.createElement("quantity");
			child.setTextContent(String.valueOf(quantity));
			root.appendChild(child);
			child = doc.createElement("description");
			child.setTextContent(description);
			root.appendChild(child);
			Node node = doc.importNode(category.toXML(false).getDocumentElement(), true);
			root.appendChild(node);
			if (subCategory != null) {
				node = doc.importNode(subCategory.toXML(false).getDocumentElement(), true);
				root.appendChild(node);
			}
			if (caseStyle != null) {
				node = doc.importNode(caseStyle.toXML(false).getDocumentElement(), true);
				root.appendChild(node);
			}
			
			// Populate properties in case we actually want it.
			if (verbose) {
				node = doc.importNode(new FormattableCollection("properties",
						properties).toXML().getDocumentElement(), true);
				root.appendChild(node);
				if (image != null) {
					node = doc.importNode(image.toXML(true).getDocumentElement(), true);
					root.appendChild(node);
				}
			} else {
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
			builder.setHeader("id", "name", "quantity", "description", "category",
					"subcategory", "package");
			if (verbose)
				builder.setHeader("id", "name", "quantity", "description", "category",
						"subcategory", "package", "properties");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				if (!verbose) {
					csv.printRecord(id, name, quantity, description, category.toString(),
							subCategory.toString(), caseStyle.toString());
				} else {
					csv.printRecord(id, name, quantity, description, category.toString(),
							subCategory.toString(), caseStyle.toString(),
							StringUtils.join(properties, System.lineSeparator()));
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
		buffer.append("Quantity: " + quantity + System.lineSeparator());
		buffer.append("Description: " + description + System.lineSeparator());
		buffer.append("Category: " + category + System.lineSeparator());
		buffer.append("Sub-Category: " + subCategory + System.lineSeparator());
		buffer.append("Package: " + caseStyle + System.lineSeparator());
		buffer.append(new FormattableCollection("Properties", properties).toPlainText());
		
		return buffer.toString();
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("ID");
		headers.add("Name");
		headers.add("Quantity");
		headers.add("Description");
		headers.add("Category");
		headers.add("Sub-Category");
		headers.add("Package");
		
		if (verbose)
			headers.add("Properties");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(String.valueOf(id));
		columns.add(name);
		columns.add(String.valueOf(quantity));
		columns.add(description);
		columns.add(category.toString());
		columns.add(subCategory.toString());
		columns.add(caseStyle.toString());
		
		if (verbose)
			columns.add(StringUtils.join(properties, System.lineSeparator()));
		
		return columns;
	}
}
