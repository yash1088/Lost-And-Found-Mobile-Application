package com.example.myapplication;

public class User {
    private String FirstName;
    private String LastName;
    private String UserID;
    private String MobileNo;

    public User(String firstName, String lastName, String userID, String mobileNo) {
        FirstName = firstName;
        LastName = lastName;
        UserID = userID;
        MobileNo = mobileNo;
    }

    public User(){

    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo)
    {
        MobileNo = mobileNo;
    }
}
