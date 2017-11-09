package com.example.student.cooper_assign2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aaron Cooper on 10/24/2017.
 */

public class Task implements Parcelable {
    private String taskName;
    private int taskID;
    private String description;


    public Task()
    {

    }
    //overloaded constructor
    public Task(String name, String descr)
    {
        taskName = name;
        description = descr;
    }

    public Task(Parcel in) {
        taskName = in.readString();
        taskID = in.readInt();
        description = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(taskName);
        parcel.writeInt(taskID);
        parcel.writeString(description);
    }
}
