package com.example.student.cooper_assign2;
/***************************************************
 * Chris Frye, Aaron Cooper
 * Created: 9/28/2017    Modified: 10/02/2017
 * Purpose: This class acts as a helper class to interact
 * with the database
 ***************************************************/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    //constant to hold the database version
    private static final int DATABASE_VERSION = 13;
    //Name of database and tables it contains

    //Table strings
    private static final String DATABASE_NAME = "TeacherStudentDB";
    private static final String TEACHER_TABLE = "Teacher_Table";
    private static final String STUDENT_TABLE = "Student_Table";
    private static final String TASK_TABLE = "Task_Table";
    private static final String COMPLETED_TASK_TABLE = "Completed_Task";

    //Define the column (fields) names for the Teacher table
    private static final String TEACHER_ID = "teacherID";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String TEACHER_IMAGE = "teacherImage";

    //Define the column (fields) names for the Student table
    private static final String STUDENT_ID = "studentID";
    private static final String S_FIRST_NAME = "firstName";
    private static final String S_LAST_NAME = "lastName";
    private static final String AGE = "age";
    private static final String S_TEACHER_ID = "teacherID";
    private static final String YEAR = "year";
    private static final String STUDENT_IMAGE = "studentImage";

    //Define fields for task table
    private static final String TASK_ID = "taskID";
    private static final String TASK_NAME = "taskName";
    private static final String DESCRIPTION = "taskDescription";
    private static final String TASK_IMAGE = "taskImage";

    //Define fields for CompletedTask table
    //Needs studentID, taskID, TimeStarted, TimeCompleted
    private static final String TIME_STARTED = "time_Started";
    private static final String TIME_COMPLETED = "time_Completed";
    private static final String DATE_COMPLETED = "date_Completed";
    private static final String TIME_SPENT = "time_Spent_On_Task";



    //CONSTRUCTOR
    public DBHelper (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //ONCREATE method
    //creates both the student and the teacher tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        //create the teacher table
        String teacherTable = "CREATE TABLE " + TEACHER_TABLE +
                "(" +
                TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIRST_NAME + " TEXT, " +
                LAST_NAME + " TEXT, " +
                EMAIL + " TEXT, " +
                PHONE_NUMBER + " TEXT, " +
                TEACHER_IMAGE + " BLOB" +
                ")";
        //create the student table
        String studentTable = "CREATE TABLE " + STUDENT_TABLE +
                "(" +
                STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                S_FIRST_NAME + " TEXT, " +
                S_LAST_NAME + " TEXT, " +
                AGE + " INTEGER, " +
                YEAR + " TEXT, " +
                S_TEACHER_ID + " INTEGER, " +
                STUDENT_IMAGE + " BLOB, " +
                "FOREIGN KEY (" + S_TEACHER_ID + ") REFERENCES " +
                TEACHER_TABLE +  "(" + TEACHER_ID + ")" +
                ")";

        String taskTable = "CREATE TABLE " + TASK_TABLE +
                "(" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_NAME + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                TASK_IMAGE + " BLOB" +
                ")";

        String completedTaskTable = "CREATE TABLE " + COMPLETED_TASK_TABLE +
                "(" +
                STUDENT_ID + " INTEGER, "
                + TASK_ID + " INTEGER, "
                + TIME_STARTED + " TEXT, "
                + TIME_COMPLETED + " TEXT, "
                + DATE_COMPLETED + " TEXT, "
                + TIME_SPENT + " TEXT, "
                + S_TEACHER_ID + " INTEGER, " +
                "PRIMARY KEY(" + STUDENT_ID + ", " + TASK_ID + ", " +
                TIME_STARTED + ", " + TIME_COMPLETED  + "))";
        db.execSQL("PRAGMA foreign_keys=1;");
        db.execSQL(teacherTable);
        db.execSQL(studentTable);
        db.execSQL(taskTable);
        db.execSQL(completedTaskTable);
        String addAdmin = "INSERT INTO " + TEACHER_TABLE + "(" + FIRST_NAME + "," + LAST_NAME + "," + EMAIL + "," + PHONE_NUMBER
                + "," + TEACHER_IMAGE + ")" + "VALUES('ADMIN', '', 'ADMIN', '', NULL)";
        db.execSQL(addAdmin);
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        //Drop the older tables if they exists
        database.execSQL("DROP TABLE IF EXISTS " + TEACHER_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + COMPLETED_TASK_TABLE);

        //recreate the tables
        onCreate(database);
    }

    //************DATEBASE OPERATIONS****************
    //Datebase ops: add, delete, and show all students and teachers


    //adding a new student
    public void addStudent(Student pStudent)
    {

        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //add student information to the database
        values.put(S_FIRST_NAME, pStudent.getFirstName());//add first name
        values.put(S_LAST_NAME, pStudent.getLastName());//add last name
        values.put(AGE, pStudent.getAge());//add age
        values.put(YEAR, pStudent.getYear());//add year
        values.put(S_TEACHER_ID, pStudent.getTeacherID());//add teacher id
        values.put(STUDENT_IMAGE, pStudent.getStudentImage());
        //insert the row in the table

        long row_id = db.insert(STUDENT_TABLE, null, values);

        //close the database connection
        db.close();
    }

    //adding a new student
    //columns: studentID, taskID, timeStarted, timeCompleted, dateCompleted
    public void addCompletedTask(Student pStudent, Task aTask, String start, String finish, String date, String timeSpent)
    {

        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //add student information to the database
        values.put(STUDENT_ID, pStudent.getStudentID());    //add studentID
        values.put(TASK_ID, aTask.getTaskID());     //add taskID
        values.put(TIME_STARTED, start);    //add start time
        values.put(TIME_COMPLETED, finish); //add finish time
        values.put(DATE_COMPLETED, date);   //add date
        values.put(TIME_SPENT, timeSpent);  //adds time spent in MS
        values.put(S_TEACHER_ID, pStudent.getTeacherID());  //add teacherID (for ordering)

        //insert the row in the table

        long row_id = db.insert(COMPLETED_TASK_TABLE, null, values);

        //close the database connection
        db.close();
    }


    //adding a new teacher
    public void addTeacher(Teacher pTeacher)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //add teacher information to the database
        values.put(FIRST_NAME, pTeacher.getFirstName());
        values.put(LAST_NAME, pTeacher.getLastName());
        values.put(EMAIL, pTeacher.getEmail());
        values.put(PHONE_NUMBER, pTeacher.getPhoneNum());
        values.put(TEACHER_IMAGE, pTeacher.getTeacherImage());
        //insert the row in the table
        db.insert(TEACHER_TABLE, null, values);

        //close the database connection
        db.close();
    }

    //adding a new task
    public void addTask(Task pTask)
    {
        //get a ref to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //add teacher information to the database
        values.put(TASK_NAME,pTask.getTaskName());
        values.put(DESCRIPTION, pTask.getDescription());
        values.put(TASK_IMAGE, pTask.getTaskImage());

        db.insert(TASK_TABLE, null, values);

        //close db connection
        db.close();
    }


    //get all teachers
    public List<Teacher> getAllTeachers()
    {
        //create a list of Teacher objects
        List<Teacher> teachers = new ArrayList<Teacher>();

        //select all query from the Teacher table
        //gets all teachers other than the admin
        String selectQuery = "SELECT * FROM " + TEACHER_TABLE + " WHERE " + TEACHER_ID + " <> 1";

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through the teachers and:
        //*create a new Teacher obj and instantiate it
        //*set the attributes for that obj
        //*add the object to the list
        //*move the cursor to the next item
        if(cursor.moveToFirst()) {
            do {
                //create a new Teacher obj
                Teacher teacher = new Teacher();
                //set the attributes
                teacher.setId(cursor.getInt(0));
                teacher.setFirstName(cursor.getString(1));
                teacher.setLastName(cursor.getString(2));
                teacher.setEmail(cursor.getString(3));
                teacher.setPhoneNum(cursor.getString(4));
                teacher.setTeacherImage(cursor.getBlob(5));
                //add the teacher object to the list
                teachers.add(teacher);
            }while(cursor.moveToNext());
        }
        //return the list of teachers

        return teachers;
    }


    //gets all completed tasks by teacher
    public List<Completed_Task> getCompletedTasksByTeacher(Teacher teacher)
    {
        //create a list of Teacher objects
        List<Completed_Task> tasks = new ArrayList<Completed_Task>();

        //select all query from the Teacher table
        String selectQuery = "SELECT * FROM " + COMPLETED_TASK_TABLE + " WHERE " + S_TEACHER_ID + " = " + teacher.getId()
                + " ORDER BY " + STUDENT_ID;

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through the task attributes and input the values necessary to input to db
        if(cursor.moveToFirst()) {
            do {
                //create a new completed_task object
                Completed_Task completed_task = new Completed_Task();
                //set the attributes
                completed_task.setStudentID(cursor.getInt(0));
                completed_task.setTaskID(cursor.getInt(1));
                completed_task.setTimeStarted(cursor.getString(2));
                completed_task.setTimeCompleted(cursor.getString(3));
                completed_task.setDate_completed(cursor.getString(4));
                completed_task.setTimeSpent(cursor.getString(5));
                //add the completed task
                tasks.add(completed_task);
            }while(cursor.moveToNext());
        }
        //return the list of tasks

        return tasks;
    }

    //getTasksByStudent
    //gets all tasks completed by a single student
    public List<Completed_Task> getCompletedTasksByStudent(Student student)
    {
        //create a list of Teacher objects
        List<Completed_Task> tasks = new ArrayList<Completed_Task>();

        //select all query from the Teacher table
        String selectQuery = "SELECT * FROM " + COMPLETED_TASK_TABLE +
                " WHERE " + STUDENT_ID + " = " + student.getStudentID();

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through the teachers and:
        //*create a new Teacher obj and instantiate it
        //*set the attributes for that obj
        //*add the object to the list
        //*move the cursor to the next item
        if(cursor.moveToFirst()) {
            do {
                //create a new completed_task object
                Completed_Task completed_task = new Completed_Task();
                //set the attributes
                completed_task.setStudentID(cursor.getInt(0));
                completed_task.setTaskID(cursor.getInt(1));
                completed_task.setTimeStarted(cursor.getString(2));
                completed_task.setTimeCompleted(cursor.getString(3));
                completed_task.setDate_completed(cursor.getString(4));
                completed_task.setTimeSpent(cursor.getString(5));
                //add the completed task
                tasks.add(completed_task);
            }while(cursor.moveToNext());
        }
        //return the list of tasks

        return tasks;
    }

    //show all students
    public List<Student> getAllStudents()
    {
        //create a list of student objects
        List<Student> students = new ArrayList<Student>();

        //select all query from the Student table
        String selectQuery = "SELECT * FROM " + STUDENT_TABLE;

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through the students and:
        //*create a new Student obj and instantiate it
        //*set the attributes for that obj
        //*add the object to the list
        //*move the cursor to the next item
        if(cursor.moveToFirst()) {
            do {
                //create a new Student obj
                Student student = new Student();
                //set the attributes
                student.setStudentID(cursor.getInt(0));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                student.setAge(cursor.getInt(3));
                student.setYear(cursor.getString(4));
                student.setTeacherID(cursor.getInt(5));
                student.setStudentImage((cursor.getBlob(6)));
                //add the student object to the list
                students.add(student);
            }while(cursor.moveToNext());

        }
        //return the list of teachers
        return students;
    }

    public Student getStudent(int id)
    {
        //create a list of student objects
        Student student = null;

        //select query from the Student table
        String selectQuery = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + STUDENT_ID + " = " + id ;

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through the students and:
        //*create a new Student obj and instantiate it
        //*set the attributes for that obj
        //*add the object to the list
        //*move the cursor to the next item
        if(cursor.moveToFirst()) {
            do {
                //create a new Student obj
                student = new Student();
                //set the attributes
                student.setStudentID(cursor.getInt(0));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                student.setAge(cursor.getInt(3));
                student.setYear(cursor.getString(4));
                student.setTeacherID(cursor.getInt(5));
                student.setStudentImage((cursor.getBlob(6)));
            }while(cursor.moveToNext());

        }
        //return the student
        return student;
    }
    public Task getTask(int id)
    {
        //create a list of student objects
        Task task = null;

        //select querty from task table
        String selectQuery = "SELECT * FROM " + TASK_TABLE + " WHERE " + TASK_ID + " = " + id ;

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through the students and:
        //*create a new Student obj and instantiate it
        //*set the attributes for that obj
        //*add the object to the list
        //*move the cursor to the next item
        if(cursor.moveToFirst()) {
            do {
                //create a new Student obj
                task = new Task();
                //set the attributes
                task.setTaskID(cursor.getInt(0));
                task.setTaskName(cursor.getString(1));
                task.setDescription(cursor.getString(2));
                task.setTaskImage(cursor.getBlob(3));
            }while(cursor.moveToNext());

        }
        //return the task
        return task;
    }

    public Teacher getTeacher(int id)
    {
        //create a teacher object
        Teacher teacher = null;

        //select all query from the Teacher table
        String selectQuery = "SELECT * FROM " + TEACHER_TABLE + " WHERE " + TEACHER_ID + " = " + id ;

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //*create a new teacher obj and instantiate it
        //*set the attributes for that obj
        //instantiate the teacher obj
        if(cursor.moveToFirst()) {
            do {
                //create a new Teacher obj
                teacher = new Teacher();
                //set the attributes
                teacher.setId(cursor.getInt(0));
                teacher.setFirstName(cursor.getString(1));
                teacher.setLastName(cursor.getString(2));
                teacher.setEmail(cursor.getString(3));
                teacher.setPhoneNum(cursor.getString(4));
                teacher.setTeacherImage(cursor.getBlob(5));
                //add the teacher object to the list
            }while(cursor.moveToNext());
        }

        //return the list of teachers
        return teacher;
    }

    //get all tasks
    public List<Task> getAllTasks()
    {

        //create a list of tasks
        List<Task> tasks = new ArrayList<Task>();
        //select all query from the task table
        String selectQuery = "SELECT * FROM " + TASK_TABLE;
        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create cursor object to take data from database
        Cursor cursor = db.rawQuery(selectQuery, null);
        //use cursor to move through rows and add the
        //records to task objects then add tasks
        //to the task list
        if(cursor.moveToFirst()) {
            do{
                Task aTask = new Task();
                aTask.setTaskID(cursor.getInt(0));
                aTask.setTaskName(cursor.getString(1));
                aTask.setDescription(cursor.getString(2));
                aTask.setTaskImage(cursor.getBlob(3));

                tasks.add(aTask);
            }while(cursor.moveToNext());
        }

        return tasks;
    }

    //show all students whose teacher ID field matches the ID of the selected teacher
    public List<Student> getStudentsByTeacher(Teacher teacher)
    {
        //create a list of student objects
        List<Student> students = new ArrayList<Student>();

        //select all query from the Student table
        String selectQuery = "SELECT * FROM " + STUDENT_TABLE + " WHERE " + S_TEACHER_ID + " = " + teacher.getId();

        //get a reference to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a cursor object to take data from the database and display
        //it in a list
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through the students and:
        //*create a new Student obj and instantiate it
        //*set the attributes for that obj
        //*add the object to the list
        //*move the cursor to the next item
        if(cursor.moveToFirst()) {
            do {
                //create a new Student obj
                Student student = new Student();
                //set the attributes
                student.setStudentID(cursor.getInt(0));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                student.setAge(cursor.getInt(3));
                student.setYear(cursor.getString(4));
                student.setTeacherID(cursor.getInt(5));
                student.setStudentImage(cursor.getBlob(6));

                //add the student object to the list
                students.add(student);
            }while(cursor.moveToNext());

        }
        //return the list of teachers
        return students;
    }

    //remove all teachers
    public void removeAllTeachers(List<Teacher> teachers)
    {
        //remove all teachers from the list
        teachers.clear();

        //get a ref to database
        SQLiteDatabase db = this.getWritableDatabase();
        //delete the teacher table (except for admin)
        db.delete(TEACHER_TABLE, "WHERE " + TEACHER_ID + " <> 1", new String[]{});
        //close the database connection
        db.close();
    }

    //remove all students
    public void removeAllStudents(List<Student> students)
    {
        //clear the student list
        students.clear();
        //get ref to database
        SQLiteDatabase db = this.getWritableDatabase();
        //delete the student table
        db.delete(STUDENT_TABLE, null, new String[]{});
        //close the database connection
        db.close();
    }

    //remove all tasks
    public void removeAllTasks(List<Task> tasks)
    {
        //clear the task list
        tasks.clear();
        //get ref to database
        SQLiteDatabase db = this.getWritableDatabase();
        //delete the task table
        db.delete(TASK_TABLE, null, new String[]{});
        //close the db connection
        db.close();

    }




    //delete a student
    //takes a student id as a parameter
    //returns true if the student was successfully removed
    //returns false if the student was not successfully removed
    public boolean removeStudent(Student aStudent)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        //ContentValues values = new ContentValues();

        db.execSQL("DELETE FROM " + STUDENT_TABLE + " WHERE " + STUDENT_ID + " = " + aStudent.getStudentID());

        db.close();
        return true;
    }

    //delete a teacher
    //takes a teacher id as a parameter
    //returns true if the student was successfully removed
    public boolean removeTeacher(Teacher aTeacher)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.execSQL("DELETE FROM " + TEACHER_TABLE + " WHERE " + TEACHER_ID + " = " + aTeacher.getId());
        db.close();
        return true;
    }

    //delete a task
    public boolean removeTask(Task aTask)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.execSQL("DELETE FROM " + TASK_TABLE + " WHERE " + TASK_ID + " = " + aTask.getTaskID());

        db.close();
        return true;
    }

    //update a teacher
    //takes a teacher obj as a parameter
    //updates the teacher based on its id
    public boolean updateTeacher(Teacher aTeacher)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIRST_NAME, aTeacher.getFirstName());
        values.put(LAST_NAME, aTeacher.getLastName());
        values.put(EMAIL, aTeacher.getEmail());
        values.put(PHONE_NUMBER, aTeacher.getPhoneNum());
        values.put(TEACHER_IMAGE, aTeacher.getTeacherImage());
        String whereClause = TEACHER_ID + "=" + aTeacher.getId();
        db.update(TEACHER_TABLE, values, whereClause, null);

        return true;
    }

    //update a student
    //takes a student obj as a parameter
    //updates the student based on its id
    public boolean updateStudent(Student aStudent)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(S_FIRST_NAME, aStudent.getFirstName());
        values.put(S_LAST_NAME, aStudent.getLastName());
        values.put(AGE, aStudent.getAge());
        values.put(S_TEACHER_ID, aStudent.getTeacherID());
        values.put(STUDENT_IMAGE, aStudent.getStudentImage());
        values.put(YEAR, aStudent.getYear());
        String whereClause = STUDENT_ID + "=" + aStudent.getStudentID();
        db.update(STUDENT_TABLE, values, whereClause, null);

        return true;
    }

    //update a task
    //takes a task obj as a parameter
    //updates the task based on its id
    public boolean updateTask(Task aTask)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK_NAME, aTask.getTaskName());
        values.put(DESCRIPTION, aTask.getDescription());
        values.put(TASK_IMAGE, aTask.getTaskImage());
        String whereClause = TASK_ID + "=" + aTask.getTaskID();
        db.update(TASK_TABLE, values, whereClause, null);

        return true;
    }
}
