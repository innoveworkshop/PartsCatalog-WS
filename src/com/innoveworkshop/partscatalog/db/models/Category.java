package com.innoveworkshop.partscatalog.db.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A component category database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "comp_categories")
public class Category {
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
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
}
