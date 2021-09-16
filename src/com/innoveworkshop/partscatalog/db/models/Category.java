package com.innoveworkshop.partscatalog.db.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
}
