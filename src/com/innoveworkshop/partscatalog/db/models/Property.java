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
 * A component property database object model.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
@Entity
@Table(name = "comp_properties")
public class Property {
	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "value")
	private String value;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id", nullable = false)
	private Component component;
	
	/**
	 * Component property empty constructor.
	 */
	public Property() {
	}
	
	/**
	 * Gets the component property ID.
	 * 
	 * @return Component property ID.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the component property ID.
	 * 
	 * @param id Component property ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the component property name.
	 * 
	 * @return Component property name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the component property name.
	 * 
	 * @param name Component property name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the component property value.
	 * 
	 * @return Component property value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the component property value.
	 * 
	 * @param value Component property value.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the parent component that this property belongs to.
	 * 
	 * @return Parent component.
	 */
	public Component getComponent() {
		return component;
	}
	
	/**
	 * Sets the parent component that this property belongs to.
	 * 
	 * @param component Parent component.
	 */
	public void setComponent(Component component) {
		this.component = component;
	}
	
	/**
	 * String representation of this object.
	 * 
	 * @return Component property name and value separated by a colon.
	 */
	@Override
	public String toString() {
		return name + ": " + value;
	}
}
