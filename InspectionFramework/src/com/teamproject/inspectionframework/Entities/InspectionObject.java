package com.teamproject.inspectionframework.Entities;

/**
 * InspectionObject object with attributes and methods for inspection objects
 *
 */
public class InspectionObject {

	// VAR-Declaration
	String id;
	String objectName;
	String description;
	String location;
	String customerName;

	// Constructor
	public InspectionObject() {
	}

	public InspectionObject(String id, String name, String description, String location, String customerName) {
		this.id = id;
		this.objectName = name;
		this.description = description;
		this.location = location;
		this.customerName = customerName;
	}

	// Setter

	public void setId(String id) {
		this.id = id;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	// Getter

	public String getId() {
		return id;
	}

	public String getObjectName() {
		return objectName;
	}

	public String getDescription() {
		return description;
	}

	public String getLocation() {
		return location;
	}

	public String getCustomerName() {
		return customerName;
	}
}
