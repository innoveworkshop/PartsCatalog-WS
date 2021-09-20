package com.innoveworkshop.partscatalog.servlets.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

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

/**
 * Provides a way to easily serialize a collection.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class FormattableCollection extends Formattable {
	private String keyName;
	private Collection<? extends Formattable> collection;
	
	/**
	 * Creates the formattable collection object.
	 * 
	 * @param keyName    Name of the key that should be used to identify this
	 *                   collection when serialized.
	 * @param collection Collection that will be serialized.
	 */
	public FormattableCollection(String keyName, Collection<? extends Formattable> collection) {
		this.keyName = keyName;
		this.collection = collection;
	}
	
	/**
	 * Serializes the collection as a JSON array.
	 * 
	 * @param  verbose Should this JSON array contain everything possible?
	 * @return         JSON array representation of the collection.
	 */
	public JSONArray toJSONArray(boolean verbose) {
		JSONArray array = new JSONArray();
		
		// Go through the collection converting it to JSON objects on the way.
		if (collection != null) {
			for (Formattable obj : collection)
				array.put(obj.toJSON(verbose));
		}
		
		return array;
	}
	
	/**
	 * Serializes the collection as a non-verbose JSON array.
	 * 
	 * @return Non-verbose JSON array representation of the collection.
	 */
	public JSONArray toJSONArray() {
		return toJSONArray(false);
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		json.put(keyName, toJSONArray(verbose));
		
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
			Element root = doc.createElement(keyName);
			doc.appendChild(root);
			
			// Go through the collection converting it to XML elements on the way.
			if (collection != null) {
				for (Formattable obj : collection) {
					Node node = doc.importNode(obj.toXML(verbose).getDocumentElement(), true);
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
			StringWriter writer = new StringWriter();
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
				csv.printRecord(collection.iterator().next().getTableHeaders(verbose));
				for (Formattable obj : collection) {
					csv.printRecord(obj.getTableRow(verbose));
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
		StringBuilder buffer = new StringBuilder();
		
		// Build list as text.
		buffer.append(keyName + ":" + System.lineSeparator());
		for (Formattable obj : collection)
			buffer.append("\t" + obj + System.lineSeparator());
		
		return buffer.toString();
	}

	/**
	 * @deprecated
	 */
	@Override
	public List<String> getTableHeaders(boolean verbose) {
		return null;
	}

	/**
	 * @deprecated
	 */
	@Override
	public List<String> getTableRow(boolean verbose) {
		return null;
	}
	
	/**
	 * Gets the object key name.
	 * 
	 * @return Object key name.
	 */
	public String getKeyName() {
		return keyName;
	}
	
	/**
	 * Sets the object key name.
	 * 
	 * @param keyName Object key name.
	 */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
}
