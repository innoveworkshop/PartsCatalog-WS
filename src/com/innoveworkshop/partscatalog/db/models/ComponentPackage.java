package com.innoveworkshop.partscatalog.db.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A component package database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "comp_packages")
public class ComponentPackage {
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	/**
	 * Component package empty constructor.
	 */
	public ComponentPackage() {
	}
	
	/**
	 * Gets the package category ID.
	 * 
	 * @return Package ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the package category ID.
	 * 
	 * @param id Package ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the package category name.
	 * 
	 * @return Package name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the package category name.
	 * 
	 * @param name Package name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Package name.
	 */
	@Override
	public String toString() {
		return name;
	}
}
