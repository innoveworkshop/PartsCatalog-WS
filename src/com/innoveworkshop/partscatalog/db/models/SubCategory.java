package com.innoveworkshop.partscatalog.db.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A component sub-category database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "comp_subcategories")
public class SubCategory {
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = true)
	private Category parentCategory;
	
	/**
	 * Component sub-category empty constructor.
	 */
	public SubCategory() {
	}
	
	/**
	 * Gets the sub-category category ID.
	 * 
	 * @return Sub-category ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the sub-category category ID.
	 * 
	 * @param id Sub-category ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the sub-category category name.
	 * 
	 * @return Sub-category name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the sub-category category name.
	 * 
	 * @param name Sub-category name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the parent category this belongs to.
	 * 
	 * @return Parent category.
	 */
	public Category getParentCategory() {
		return parentCategory;
	}
	
	/**
	 * Sets the parent category this belongs to.
	 * 
	 * @param parent Parent category.
	 */
	public void setParentCategory(Category parent) {
		this.parentCategory = parent;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Sub-category name.
	 */
	@Override
	public String toString() {
		return name;
	}
}
