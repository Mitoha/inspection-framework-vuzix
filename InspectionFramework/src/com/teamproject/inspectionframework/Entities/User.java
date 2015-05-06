package com.teamproject.inspectionframework.Entities;

public class User {

    //VAR-declaration
    String userId;
    String userName;
    String email;
    String role;
    String firstName;
    String lastName;
    String phoneNumber;
    String mobileNumber;


    //Constructor
    public User() {
    }
    
    public User(String userId, String userName, String email, String role, String firstName, String lastName, String phoneNumber, String mobileNumber) {
    	this.userId=userId;
    	this.userName=userName;
    	this.email=email;
    	this.role=role;
    	this.firstName=firstName;
    	this.lastName=lastName;
    	this.phoneNumber=phoneNumber;
    	this.mobileNumber=mobileNumber;
    }

    //Getter and setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}

