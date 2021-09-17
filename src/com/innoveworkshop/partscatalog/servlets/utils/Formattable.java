package com.innoveworkshop.partscatalog.servlets.utils;

import java.util.List;

import org.json.JSONObject;
import org.w3c.dom.Document;

/**
 * Abstract class to define objects that can be properly formatted when
 * serialized by using {@link ServletResponseFormatter}.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 * @see ServletResponseFormatter#getFormattedResponse(Object)
 */
public abstract class Formattable {
	/**
	 * Serializes this object into XML.
	 * 
	 * @param  verbose Should this XML document contain everything possible?
	 * @return         XML representation of this object.
	 */
	abstract public Document toXML(boolean verbose);
	
	/**
	 * Serializes this object into a non-verbose XML document.
	 * 
	 * @return Non-verbose XML representation of this object.
	 */
	public final Document toXML() {
		return toXML(false);
	}
	
	/**
	 * Serializes this object into JSON.
	 * 
	 * @param  verbose Should this JSON object contain everything possible?
	 * @return         JSON representation of this object.
	 */
	abstract public JSONObject toJSON(boolean verbose);
	
	/**
	 * Serializes this object into a non-verbose JSON.
	 * 
	 * @return Non-verbose JSON representation of this object.
	 */
	public final JSONObject toJSON() {
		return toJSON(false);
	}
	
	/**
	 * Serializes this object into a human-readable plain text representation.
	 * 
	 * @param  verbose Should this text contain everything possible?
	 * @return         Human-readable representation of this object.
	 */
	abstract public String toPlainText(boolean verbose);
	
	/**
	 * Serializes this object into a non-verbose human-readable plain text
	 * representation.
	 * 
	 * @return Non-verbose human-readable representation of this object.
	 */
	public final String toPlainText() {
		return toPlainText(false);
	}
	
	/**
	 * Serializes this object into a CSV string representation.
	 * 
	 * @param  verbose Should this CSV string contain everything possible?
	 * @return         CSV representation of this object.
	 */
	abstract public String toCSV(boolean verbose);
	
	/**
	 * Serializes this object into a non-verbose CSV string representation.
	 * 
	 * @return Non-verbose CSV representation of this object.
	 */
	public final String toCSV() {
		return toCSV(false);
	}
	
	/**
	 * Gets the table headers used for CSV and HTML output formats.
	 * 
	 * @param  verbose Should this list contain everything possible?
	 * @return         List of table header names.
	 */
	abstract public List<String> getTableHeaders(boolean verbose);
	
	/**
	 * Gets the table row used for CSV and HTML output formats.
	 * 
	 * @param  verbose Should this list contain everything possible?
	 * @return         List of table column values.
	 */
	abstract public List<String> getTableRow(boolean verbose);
}
