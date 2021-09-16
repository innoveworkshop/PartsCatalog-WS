package com.innoveworkshop.partscatalog.db.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A component database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "components")
public class Component {
	@Id @GeneratedValue
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
	@JoinColumn(name = "subcategory_id", nullable = true)
	private SubCategory subcategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", nullable = true)
	private ComponentPackage caseStyle;
	
	@ManyToMany(mappedBy = "component", fetch = FetchType.LAZY)
	private Set<Property> properties;
	
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
		return subcategory;
	}
	
	/**
	 * Sets the sub-category this component belongs to.
	 * 
	 * @param subcategory Component sub-category.
	 */
	public void setSubCategory(SubCategory subcategory) {
		this.subcategory = subcategory;
	}
	
	/**
	 * Gets the component package.
	 * 
	 * @return Component package.
	 */
	public ComponentPackage getCaseStyle() {
		return caseStyle;
	}
	
	/**
	 * Sets the component package.
	 * 
	 * @param caseStyle Component package.
	 */
	public void setCaseStyle(ComponentPackage caseStyle) {
		this.caseStyle = caseStyle;
	}
	
	/**
	 * Gets the component package.
	 * 
	 * @return Component package.
	 * @see    #getCaseStyle()
	 */
	public ComponentPackage getPackage() {
		return getCaseStyle();
	}
	
	/**
	 * Sets the component package.
	 * @see #setCaseStyle(ComponentPackage)
	 * 
	 * @param caseStyle Component package.
	 */
	public void setPackage(ComponentPackage caseStyle) {
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
	 * String representation of this object.
	 * 
	 * @return Component name.
	 */
	@Override
	public String toString() {
		return name;
	}
}
