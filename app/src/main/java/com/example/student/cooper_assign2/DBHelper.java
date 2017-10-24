package com.example.student.cooper_assign2;
/***************************************************
 * Chris Frye, Aaron Cooper, Chris Foose
 * Created: 9/28/2017    Modified: 10/02/2017
 * Purpose: This class acts as a helper class to interact
 * with the database
 ***************************************************/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    //constant to hold the database verson
    private static final int DATABASE_VERSION = 1;
    //Name of database and two tables it contains
    //Table for teachers and table for students
    private static final String DATABASE_NAME = "TeacherStudentDB";
    private static final String TEACHER_TABLE = "Teacher_Table";
    private static final String STUDENT_TABLE = "Student_Table";

    //Define the column (fields) names for the Teacher table
    private static final String TEACHER_ID = "teacherID";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String PHONE_NUMBER = "phoneNumber";

    //Define the column (fields) names for the Student table
    private static final String STUDENT_ID = "studentID";
    private static final String S_FIRST_NAME = "firstName";
    private static final String S_LAST_NAME = "lastName";
    private static final String AGE = "age";
    private static final String S_TEACHER_ID = "teacherID";
    private static final String YEAR = "year";


    //CONSTRUCTOR
    public DBHelper (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //ONCREATE method
    //creates both the student and the teacher tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        //create the teacher table
        String teacherTable = "CREATE TABLE " + TEACHER_TABLE + "("
                + TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIRST_NAME
                + " TEXT, " + LAST_NAME + " TEXT, " + EMAIL + " TEXT, "
                + PHONE_NUMBER + " TEXT" + ")";
        //create the student table
        String studentTable = "CREATE TABLE " + STUDENT_TABLE +
                "(" +
                STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                S_FIRST_NAME + " TEXT, " +
                S_LAST_NAME + " TEXT, " +
                AGE + " INTEGER, " +
                YEAR + " TEXT, " +
                S_TEACHER_ID + " INTEGER " +
                ")";

        db.execSQL(teacherTable);
        db.execSQL(studentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        //Drop the older tables if they exists
        database.execSQL("DROP TABLE IF EXISTS " + TEACHER_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);

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
        //insert the row in the table

        long row_id = db.insert(STUDENT_TABLE, null, values);

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

        //insert the row in the table
        db.insert(TEACHER_TABLE, null, values);

        //close the database connection
        db.close();
    }


    //get all teachers
    public List<Teacher> getAllTeachers()
    {
        //create a list of Teacher objects
        List<Teacher> teachers = new ArrayList<Teacher>();

        //select all query from the Teacher table
        String selectQuery = "SELECT * FROM " + TEACHER_TABLE;

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
                teacher.setFullNameID();
                //add the teacher object to the list
                teachers.add(teacher);
            }while(cursor.moveToNext());
        }
        //return the list of teachers

        return teachers;
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
        //delete the teacher table
        db.delete(TEACHER_TABLE, null, new String[]{});
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


    //delete a student
    //takes a student id as a parameter
    //returns true if the student was successfully removed
    //returns false if the student was not successfully removed
    public boolean removeStudent(int studentID)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.execSQL("DELETE FROM " + STUDENT_TABLE + " WHERE STUDENT_ID = " + studentID);

        return true;
    }

    //delete a teacher
    //takes a teacher id as a parameter
    //returns true if the student was successfully removed
    public boolean removeTeacher(int teacherID)
    {
        //get a ref to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.execSQL("DELETE FROM " + TEACHER_TABLE + " WHERE TEACHER_ID = " + teacherID);

        return true;
    }
}
