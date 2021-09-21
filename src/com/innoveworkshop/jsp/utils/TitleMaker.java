package com.innoveworkshop.jsp.utils;

import com.innoveworkshop.partscatalog.config.Configuration;

/**
 * A simple utility to generate titles for JSP pages.
 *
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class TitleMaker {
	/**
	 * Gets a proper page title with the project name appended to it.
	 * 
	 * @param  title Page title.
	 * @return       Page title with project name appended to it.
	 */
	public static String getTitle(String title) {
		if (title != null) {
			if (title.isBlank())
				return Configuration.PROJECT_NAME;
			
			return title + " - " + Configuration.PROJECT_NAME;
		}
		
		return Configuration.PROJECT_NAME;
	}
}
