package com.innoveworkshop.partscatalog.servlets.utils;

import java.util.ArrayList;

import com.innoveworkshop.partscatalog.db.models.CaseStyle;
import com.innoveworkshop.partscatalog.db.models.Category;
import com.innoveworkshop.partscatalog.db.models.Component;
import com.innoveworkshop.partscatalog.db.models.SubCategory;

/**
 * A way to search for components with a simple query string.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class ComponentSearch {
	private String query;
	protected ArrayList<Object> queryComponents;
	
	/**
	 * Initializes and empty component search object.
	 */
	public ComponentSearch() {
		queryComponents = new ArrayList<Object>();
	}
	
	/**
	 * Creates a component search object with a search query already associated
	 * with it.
	 * 
	 * @param query Search query string.
	 */
	public ComponentSearch(String query) {
		this();
		setQuery(query);
	}
	
	/**
	 * Gets the HQL query string to be used to perform this search.
	 * 
	 * @return HQL query string to perform the search.
	 */
	public String getDatabaseQueryString() {
		StringBuilder buf = new StringBuilder();
		
		// Start the query up.
		buf.append("SELECT DISTINCT comp FROM Component comp ");
		buf.append("LEFT JOIN FETCH comp.category LEFT JOIN FETCH comp.subCategory ");
		buf.append("LEFT JOIN FETCH comp.caseStyle LEFT JOIN FETCH comp.properties ");
		buf.append("LEFT JOIN FETCH comp.image LEFT JOIN FETCH comp.datasheet WHERE ");
		
		// Append the filtering snippets to the HQL query.
		for (int i = 0; i < queryComponents.size(); i++) {
			buf.append(getDatabaseQueryFilterSnippet(queryComponents.get(i),
				"comp", i > 0));
		}
		
		// Make sure the results are sorted.
		buf.append(" ORDER BY comp.name");
		
		return buf.toString();
	}
	
	/**
	 * Parses the object query string and populates the query components list.
	 * 
	 * @param clearArray Should we clear the query components list?
	 */
	protected void parseQueryString(boolean clearArray) {
		// Clear the query component list first?
		if (clearArray)
			queryComponents.clear();
		
		// Split the string spaces and try to parse the special query items.
		String[] queryItems = query.split(" ");
		for (String item : queryItems) {
			// Check if it's just a double space.
			if (item.length() == 0)
				continue;
			
			// Check if we just got a simple string to search for things.
			String[] specialForm = item.split(":");
			if (specialForm.length != 2) {
				queryComponents.add(item);
				continue;
			}
			
			// Get the special form key and value pair.
			String key = specialForm[0].toLowerCase();
			String value = specialForm[1];
			
			// Try to parse which kind of object this special form represents.
			Object obj = null;
			if (key.equals("category")) {
				Category category = new Category();
				category.setID(Integer.valueOf(value));
				
				obj = category;
			} else if (key.equals("subcategory")) {
				SubCategory subCategory = new SubCategory();
				subCategory.setID(Integer.valueOf(value));
				
				obj = subCategory;
			} else if (key.equals("package")) {
				CaseStyle caseStyle = new CaseStyle();
				caseStyle.setID(Integer.valueOf(value));
				
				obj = caseStyle;
			} else if (key.equals("component")) {
				Component component = new Component();
				component.setID(Integer.valueOf(value));
				
				obj = component;
			} else {
				queryComponents.add(item);
				continue;
			}

			// Add the special form query component to the list.
			queryComponents.add(obj);
		}
	}
	
	/**
	 * Get a snippet of the HQL query string to filter the results based on the
	 * passed object.
	 * 
	 * @param  obj             Object to be used in a HQL query filter.
	 * @param  compVariable    Component object variable name used in the HQL query.
	 * @param  prependBoolOper Prepend an appropriate boolean operator to the filter.
	 * @return                 Snippet of HQL query to filter by the passed object.
	 */
	protected String getDatabaseQueryFilterSnippet(Object obj, String compVariable,
			boolean prependBoolOper) {
		StringBuilder buf = new StringBuilder();
		
		// Create a proper snippet of HQL query based on the type of the passed object.
		if (obj instanceof String) {
			// Simple string search for a component name or description.
			if (prependBoolOper)
				buf.append(" AND ");
			
			String str = (String)obj;
			buf.append(" ((" + compVariable + ".name LIKE '%" + str + "%') OR (" +
				compVariable + ".description LIKE '%" + str + "%')) ");
		} else if (obj instanceof Category) {
			// Specific category.
			if (prependBoolOper)
				buf.append(" AND ");
			
			String catID = String.valueOf(((Category)obj).getID());
			buf.append(" (" + compVariable + ".category.id = " + catID + ") ");
		} else if (obj instanceof SubCategory) {
			// Specific sub-category.
			if (prependBoolOper)
				buf.append(" AND ");
			
			String subCatID = String.valueOf(((SubCategory)obj).getID());
			buf.append(" (" + compVariable + ".subCategory.id = " + subCatID + ") ");
		} else if (obj instanceof CaseStyle) {
			// Specific package.
			if (prependBoolOper)
				buf.append(" AND ");
			
			String packID = String.valueOf(((CaseStyle)obj).getID());
			buf.append(" (" + compVariable + ".caseStyle.id = " + packID + ") ");
		} else if (obj instanceof Component) {
			// Specific component.
			if (prependBoolOper)
				buf.append(" OR ");
			
			String compID = String.valueOf(((Component)obj).getID());
			buf.append(" (" + compVariable + ".id = " + compID + ") ");
		}
		
		return buf.toString();
	}
	
	/**
	 * Gets the search query string.
	 * 
	 * @return Search query string.
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Sets the query string related to this search.
	 * 
	 * @param query Search query string.
	 */
	public void setQuery(String query) {
		this.query = query;
		parseQueryString(true);
	}
}
