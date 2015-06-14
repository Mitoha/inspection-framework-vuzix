package com.teamproject.inspectionframework.Entities;

/**
 * Assignment object with attributes and methods for assignments
 *
 */
public class Assignment {

    //VAR-Declaration
    String assignmentName;
    String id;
    String description;
    Long startDate;
    Long dueDate;
    String userId;
    String inspectionObjectId;
    String isTemplate;
    Integer state;
    Integer version;


    //Constructor
    public Assignment(){

    }
    
    public Assignment(String name, String id, String description, Long startDate, Long dueDate, String userId, String InspObjId, String isTemplate) {
    	this.assignmentName=name;
    	this.id=id;
    	this.description=description;
    	this.startDate=startDate;
    	this.dueDate=dueDate;
    	this.userId=userId;
    	this.inspectionObjectId=InspObjId;
    	this.isTemplate=isTemplate;
    	this.state=0;
    	this.version=0;
    }

    //Setter


    public void setState(Integer state) {
        this.state = state;
    }

    public void setIsTemplate(String isTemplate) {
        this.isTemplate = isTemplate;
    }

    public void setAssignmentName(String assignmentName1)
    {
        assignmentName=assignmentName1;
    }

    public void setId(String id1){
        id=id1;
    }

    public void setDescription(String description1){
        description=description1;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setInspectionObjectId(String inspectionObjectId) {
        this.inspectionObjectId = inspectionObjectId;
    }
    
    public void setVersion(Integer version) {
    	this.version = version;
    }

    //Getter
    public Integer getState() {
        return state;
    }

    public String getIsTemplate() {
        return isTemplate;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getInspectionObjectId() {
        return inspectionObjectId;
    }
    
    public Integer getVersion() {
    	return version;
    }

    @Override
    public String toString() {
        return assignmentName;
    }
}