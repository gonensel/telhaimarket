package com.example.telhaimarket;

public class User {
    private String email;
    private String uid;
    private String phoneNumber;
    private String fullName;

    public User() {
        //Empty Constructor For Firebase
    }


    public User(String email, String uid, String fullName, String phoneNumber)
    {
        this.email = email; //Parameterized for Program-Inhouse objects.
        this.uid = uid;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    //Getters and Setters
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUid ()
    {
        return uid;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }
}
