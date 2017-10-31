package com.example.student.cooper_assign2;

/***************************
 * Author: Aaron Cooper
 * Purpose: A student class to be used within
 * the database
 **************************/

public class Student {
    private int studentID;
    private String firstName;
    private String lastName;
    private int age;
    private int teacherID;
    private String year;
    private byte[] studentImage;

    public  Student()
    {

    }

    public Student(String fn, String ln, int age, int teachID, String year)
    {
        firstName = fn;
        lastName=ln;
        this.age = age;
        teacherID = teachID;
        this.year = year;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public byte[] getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(byte[] studentImage) {
        this.studentImage = studentImage;
    }
}
