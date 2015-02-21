package com.teamproject.inspectionframework.Entities;

public class Assignment {

    //Var Declaration
    String assignmentName;
    String id;
    String description;
    Integer startDate;
    Integer dueDate;
    String userId;
    String inspectionObjectId;
    String isTemplate;


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

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setInspectionObjectId(String inspectionObjectId) {
        this.inspectionObjectId = inspectionObjectId;
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



    public Integer getStartDate() {
        return startDate;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getInspectionObjectId() {
        return inspectionObjectId;
    }

    @Override
    public String toString() {
        return description;
    }
}
