package com.teamproject.inspectionframework.Entities;

public class Attachment {
	
    //Var-declaration
    String id;
    String file_type;
    Object binaryObject;

    //Constructor
    public Attachment() {
    }

    //Getter and Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public Object getBinaryObject() {
        return binaryObject;
    }

    public void setBinaryObject(Object binaryObject) {
        this.binaryObject = binaryObject;
    }
}

