package com.teamproject.inspectionframework.Entities;

public class Task {

    //Var Declaration
    String id;
    String taskName;
    String description;
    Integer state;
    String assignmentId;

    //Constructor
    public Task(){
    }


    //Setter
    public void setId(String id1){
        id=id1;
    }

    public void setTaskName(String taskName1){
        taskName=taskName1;
    }

    public void setDescription(String description1){
        description=description1;
    }

    public void setState(Integer state1){
        state=state1;
    }
    
    public void setAssignmentId(String assId) {
    	assignmentId = assId;
    }

    //Getter
    public String getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getState() {
        return state;
    }
    
    public String getAssignmentId() {
        return assignmentId;
    }
}
