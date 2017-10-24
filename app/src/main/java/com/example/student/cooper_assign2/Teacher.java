package com.example.student.cooper_assign2;

public class Teacher {
    //Instance variables
    private int teacherID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String fullNameID;

    public Teacher()
    {

    }

    public Teacher(String fn, String ln, String email, String phoneNum)
    {
        firstName = fn;
        lastName = ln;
        this.email = email;
        this.phoneNum = phoneNum;
        setFullNameID();
    }

    public int getId() {
        return teacherID;
    }

    public void setId(int id) {
        this.teacherID = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public void setFullNameID() { fullNameID = firstName + " " + lastName + "\t ID: " + teacherID;}
    public String getFullNameID(){return fullNameID;}
}
