package com.teamproject.inspectionframework.Entities;

public class InspectionObject {

    String id;
    String objectName;
    String description;
    String location;
    String customerName;

    //Constructor
    public InspectionObject() {
    }

    //Setter

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

    //Getter

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
