package com.teamproject.inspectionframework.Entities;

public class Assignment {

    //Var Declaration
    String assignmentName;
    String id;
    String description;
    Long startDate;
    Long dueDate;
    String userId;
    String inspectionObjectId;
    String isTemplate;
    Integer assignmentState;


    //Constructor
    public Assignment(){

    }

    //Setter

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

    public void setStartDate(Long l) {
        this.startDate = l;
    }

    public void setDueDate(Long l) {
        this.dueDate = l;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setInspectionObjectId(String inspectionObjectId) {
        this.inspectionObjectId = inspectionObjectId;
    }
    
    public void setAssignmentState(Integer state) {
    	this.assignmentState = state;
    }

    //Getter

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
    
    public Integer getAssignmentState() {
    	return assignmentState;
    }

    @Override
    public String toString() {
        return description;
    }
}
