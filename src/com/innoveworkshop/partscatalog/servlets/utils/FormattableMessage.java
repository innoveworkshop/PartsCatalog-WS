package com.innoveworkshop.partscatalog.servlets.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A formattable response message object to be used to send arbitrary messages
 * to a client.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class FormattableMessage extends Formattable {
	private String message;
	
	/**
	 * Creates a formattable message object with a pre-defined message.
	 * 
	 * @param message Message that will be formatted.
	 */
	public FormattableMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Gets the message string that will be formatted.
	 * 
	 * @return Unformatted message string.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message string to be formatted.
	 * 
	 * @param message Message string to be formatted.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}

	@Override
	public Document toXML(boolean verbose) {
		try {
			// Setup the XML writer.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Create the root element.
			Document doc = builder.newDocument();
			Element root = doc.createElement("message");
			doc.appendChild(root);
			
			// Populate the root element.
			Element child = doc.createElement("value");
			child.setTextContent(message);
			root.appendChild(child);
			
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public JSONObject toJSON(boolean verbose) {
		JSONObject json = new JSONObject();
		
		// Populate the JSON object.
		json.put("message", message);
		
		return json;
	}

	@Override
	public String toPlainText(boolean verbose) {
		return toString();
	}

	@Override
	public String toCSV(boolean verbose) {
		try {
			// Setup the CSV writer.
			StringWriter writer = new StringWriter();
			CSVFormat.Builder builder = CSVFormat.Builder.create(CSVFormat.DEFAULT);
			builder.setHeader("message");
			
			// Actually build the CSV row.
			try (CSVPrinter csv = new CSVPrinter(writer, builder.build())) {
				csv.printRecord(message);
				csv.flush();
			}
			
			return writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<String> getTableHeaders(boolean verbose) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("Message");
		
		return headers;
	}

	@Override
	public List<String> getTableRow(boolean verbose) {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(message);
		
		return columns;
	}
}
