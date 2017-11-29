package com.example.student.cooper_assign2;

import android.os.Parcel;
import android.os.Parcelable;

/***************************
 * Author: Aaron Cooper
 * Purpose: A student class to be used within
 * the database
 **************************/

public class Student implements Parcelable{
    private int studentID;
    private String firstName;
    private String lastName;
    private int age;
    private int teacherID;
    private String year;
    private byte[] studentImage;
    private boolean isDeletable;

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
        isDeletable = false;
    }

    //used to pass object between activities
    //this constructor is used when receiving the student object
    //does not pass the image or isDeletable values
    public Student(Parcel in)
    {
        String[] data = new String[6];
        in.readStringArray(data);
        this.studentID = Integer.parseInt(data[0]);
        this.firstName = data[1];
        this.lastName = data[2];
        this.age = Integer.parseInt(data[3]);
        this.teacherID = Integer.parseInt(data[4]);
        this.year = data[5];
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

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

    public String getFullName() {
        return getFirstName() + " " + getLastName();
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

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        //write the array that will be passed to the constructor
        //above.. ORDER IS VERY IMPORTANT HERE
        parcel.writeStringArray(new String[] {
                Integer.toString(this.studentID),// 0
                this.firstName, //1
                this.lastName,//2
                Integer.toString(this.age), //3
                Integer.toString(this.teacherID), //4
                year//5
        });
    }
}
