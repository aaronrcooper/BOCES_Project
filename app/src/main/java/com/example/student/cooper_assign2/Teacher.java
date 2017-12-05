package com.example.student.cooper_assign2;

import android.os.Parcel;
import android.os.Parcelable;

public class Teacher implements Parcelable {
    //Instance variables
    private int teacherID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private byte[] teacherImage;
    private boolean isDeletable;

    public Teacher()
    {

    }

    public Teacher(String fn, String ln, String email, String phoneNum)
    {
        firstName = fn;
        lastName = ln;
        this.email = email;
        this.phoneNum = phoneNum;
        isDeletable= false;
    }

    protected Teacher(Parcel in) {
        teacherID = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phoneNum = in.readString();
        teacherImage = in.createByteArray();
        isDeletable = in.readByte() != 0;
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

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

    public byte[] getTeacherImage() {
        return teacherImage;
    }

    public void setTeacherImage(byte[] teacherImage) {
        this.teacherImage = teacherImage;
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
        parcel.writeInt(teacherID);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(phoneNum);
        parcel.writeByteArray(teacherImage);
        parcel.writeByte((byte) (isDeletable ? 1 : 0));
    }
}
