package com.example.student.cooper_assign2;

/**
 * Created by Student on 11/20/2017.
 */

public class Completed_Task {
    private int studentID;
    private int taskID;
    private String date_completed;
    private String timeStarted;
    private String timeCompleted;
    private String timeSpent;

    Completed_Task()
    {

    }

    Completed_Task(int studentID, int taskID, String date_completed, String timeStarted, String timeCompleted, String timeSpent)
    {
        setStudentID(studentID);
        setTaskID(taskID);
        setDate_completed(date_completed);
        setTimeStarted(timeStarted);
        setTimeCompleted(timeCompleted);
        setTimeSpent(timeSpent);
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getDate_completed() {
        return date_completed;
    }

    public void setDate_completed(String date_completed) {
        this.date_completed = date_completed;
    }

    public String getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(String timeStarted) {
        this.timeStarted = timeStarted;
    }

    public String getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(String timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }
}
