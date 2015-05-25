package com.teamproject.inspectionframework.Entities;

/**
 * Attachment object with attributes and methods for attachments
 *
 */
public class Attachment {

	// VAR-declaration
	String id;
	String file_type;
	String taskId;
	String assignmentId;
	byte[] binaryObject;

	// Constructor
	public Attachment() {
	}

	// Getter and Setter

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {
		return taskId;
	}

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

	public byte[] getBinaryObject() {
		return binaryObject;
	}

	public void setBinaryObject(byte[] binaryObject) {
		this.binaryObject = binaryObject;
	}
}
