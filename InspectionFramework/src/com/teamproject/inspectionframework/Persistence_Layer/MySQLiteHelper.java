package com.teamproject.inspectionframework.Persistence_Layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.User;

import java.util.ArrayList;
import java.util.List;

   public class MySQLiteHelper extends SQLiteOpenHelper {

       //Table names
       public static final String TABLE_ASSIGNMENTS = "assignments";
       public static final String TABLE_TASKS = "tasks";
       public static final String TABLE_USERS = "users";
       public static final String TABLE_INSPECTIONOBJECTS = "inspectionobjects";

       //Column names table assignments
       public static final String A_COLUMN_ROWID = "_id";
       public static final String A_COLUMN_DESCRIPTION = "description";
       public static final String A_COLUMN_ASSIGNMENTNAME = "assignmentName";
       public static final String A_COLUMN_ASSIGNMENT_ID = "assignmentId";
       public static final String A_COLUMN_STARTDATE = "startDate";
       public static final String A_COLUMN_ENDDATE = "endDate";
       public static final String A_COLUMN_USER_ID = "userId";
       public static final String A_COLUMN_INSPECTIONOBJECT_ID = "inspectionObjectId";
       public static final String A_COLUMN_ISTEMPLATE = "isTemplate";


       //Column names table tasks
       public static final String T_COLUMN_ROWID = "_id";
       public static final String T_COLUMN_TASKNAME = "taskName";
       public static final String T_COLUMN_DESCRIPTION = "description";
       public static final String T_COLUMN_STATE = "state";
       public static final String T_TASK_ID = "taskId";
       public static final String T_PK = "PK";

       //Column names table users
       public static final String U_COLUMN_ROWID = "_id";
       public static final String U_COLUMN_USER_ID = "userId";
       public static final String U_COLUMN_USERNAME = "userName";
       public static final String U_COLUMN_EMAIL = "email";
       public static final String U_COLUMN_ROLE = "role";
       public static final String U_COLUMN_FIRSTNAME = "firstname";
       public static final String U_COLUMN_LASTNAME = "lastname";
       public static final String U_COLUMN_PHONENUMBER = "phoneNumber";
       public static final String U_COLUMN_MOBILENUMBER = "mobileNumber";

       //Column names table inspectionobjects
       public static final String I_COLUMN_ROWID = "_id";
       public static final String I_COLUMN_OBJECT_ID = "objectId";
       public static final String I_COLUMN_OBJECTNAME = "objectName";
       public static final String I_COLUMN_DESCRIPTION = "description";
       public static final String I_COLUMN_LOCATION = "location";
       public static final String I_COLUMN_CUSTOMERNAME = "customername";


       //Database information
       private static final String DATABASE_NAME = "newTestDatabase.db";
       private static final int DATABASE_VERSION = 1;

       // Assignment Table creation sql statement
       private static final String CREATE_TABLE_ASSIGNMENTS = "CREATE TABLE "
               + TABLE_ASSIGNMENTS + "(" + A_COLUMN_ROWID + " INTEGER, " + A_COLUMN_ASSIGNMENT_ID + " TEXT PRIMARY KEY UNIQUE, "
               + A_COLUMN_DESCRIPTION + " TEXT, " + A_COLUMN_ASSIGNMENTNAME + " TEXT, " + A_COLUMN_STARTDATE + " INTEGER, " + A_COLUMN_ENDDATE + " INTEGER, "
               + A_COLUMN_ISTEMPLATE + " TEXT, " + A_COLUMN_INSPECTIONOBJECT_ID + " TEXT, "  + A_COLUMN_USER_ID + " TEXT)";

       //Task Table creation sql statement
       private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
               + TABLE_TASKS + "(" + T_COLUMN_ROWID + " INTEGER, " + T_COLUMN_TASKNAME + " TEXT, " + T_COLUMN_DESCRIPTION + " TEXT, "
               + T_COLUMN_STATE + " INTEGER, " + T_TASK_ID + " TEXT PRIMARY KEY, " + T_PK + " TEXT, " + " FOREIGN KEY(PK) REFERENCES TABLE_ASSIGNMENTS(assignmentId))";

       //User Table creation sql statement
       private static final String CREATE_TABLE_USERS = "CREATE TABLE "
               + TABLE_USERS + "(" + U_COLUMN_ROWID + " INTEGER, " + U_COLUMN_USER_ID + " TEXT PRIMARY KEY UNIQUE, " + U_COLUMN_USERNAME + " TEXT, "
               + U_COLUMN_FIRSTNAME + " TEXT, " + U_COLUMN_LASTNAME + " TEXT, " + U_COLUMN_ROLE + " TEXT, " + U_COLUMN_EMAIL + " TEXT, "
               + U_COLUMN_PHONENUMBER + " TEXT, " + U_COLUMN_MOBILENUMBER + " TEXT)";

       //InspectionObject Table creation sql statement
       private static final String CREATE_TABLE_INSPECTIONOBJECTS = "CREATE TABLE "
               + TABLE_INSPECTIONOBJECTS + "(" + I_COLUMN_ROWID + " INTEGER, " + I_COLUMN_OBJECT_ID + " TEXT PRIMARY KEY UNIQUE, " + I_COLUMN_OBJECTNAME + " TEXT, "
               + I_COLUMN_DESCRIPTION + " TEXT, " + I_COLUMN_LOCATION + " TEXT, " + I_COLUMN_CUSTOMERNAME + " TEXT)";

       public MySQLiteHelper(Context context) {
               super(context, DATABASE_NAME, null, DATABASE_VERSION);
           }

           @Override
           public void onCreate(SQLiteDatabase database) {
               database.execSQL(CREATE_TABLE_ASSIGNMENTS);
               database.execSQL(CREATE_TABLE_TASKS);
               database.execSQL(CREATE_TABLE_USERS);
               database.execSQL(CREATE_TABLE_INSPECTIONOBJECTS);
               
               Log.i("IF", "onCreateDB");
                          }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           Log.w(MySQLiteHelper.class.getName(),
                   "Upgrading database from version " + oldVersion + " to "
                           + newVersion + ", which will destroy all old data");
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
           db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTIONOBJECTS);
           onCreate(db);  
           Log.i("IF", "onUpdateDB");
       }
       
       //create a row User
       public void createUser(String userId, String userName, String firstname, String lastname, String role, String email, String phoneNumber, String mobileNumber){

           SQLiteDatabase database = this.getWritableDatabase();
           ContentValues values = new ContentValues();

           values.put(MySQLiteHelper.U_COLUMN_USER_ID, userId);
           values.put(MySQLiteHelper.U_COLUMN_USERNAME, userName);
           values.put(MySQLiteHelper.U_COLUMN_FIRSTNAME, firstname);
           values.put(MySQLiteHelper.U_COLUMN_LASTNAME, lastname);
           values.put(MySQLiteHelper.U_COLUMN_ROLE, role);
           values.put(MySQLiteHelper.U_COLUMN_EMAIL, email);
           values.put(MySQLiteHelper.U_COLUMN_MOBILENUMBER, mobileNumber);
           values.put(MySQLiteHelper.U_COLUMN_PHONENUMBER, phoneNumber);

           long insertId = database.insert(MySQLiteHelper.TABLE_USERS, null, values);
       }

       //create a row Assignment
       public void createAssignment(String assignmentId, String assignmentName, String description, Integer startDate, Integer endDate, String objectId, String userId, String isTemplate) {

           SQLiteDatabase database = this.getWritableDatabase();
           ContentValues values = new ContentValues();

           values.put(MySQLiteHelper.A_COLUMN_DESCRIPTION, description);
           values.put(MySQLiteHelper.A_COLUMN_ASSIGNMENTNAME, assignmentName);
           values.put(MySQLiteHelper.A_COLUMN_ASSIGNMENT_ID, assignmentId);
           values.put(MySQLiteHelper.A_COLUMN_STARTDATE, startDate);
           values.put(MySQLiteHelper.A_COLUMN_ENDDATE, endDate);
           values.put(MySQLiteHelper.A_COLUMN_ISTEMPLATE, isTemplate);
           values.put(MySQLiteHelper.A_COLUMN_INSPECTIONOBJECT_ID, objectId);
           values.put(MySQLiteHelper.A_COLUMN_USER_ID, userId);

           //insert row
           long insertId = database.insert(MySQLiteHelper.TABLE_ASSIGNMENTS, null,
                   values);
       }

       //Create a row Task
       public void createTask(String taskId, String taskName, String description, Integer state, String PK ){

           SQLiteDatabase database = this.getWritableDatabase();
           ContentValues values = new ContentValues();

           values.put(MySQLiteHelper.T_COLUMN_TASKNAME, taskName);
           values.put(MySQLiteHelper.T_COLUMN_DESCRIPTION, description);
           values.put(MySQLiteHelper.T_COLUMN_STATE, state);
           values.put(MySQLiteHelper.T_PK, PK);

           long insertId = database.insert(MySQLiteHelper.TABLE_TASKS, null,
                   values);
       }

       //Create a row InspectionObject
       public void createInspectionObject(String objectId, String objectName, String description, String location, String customerName){

           SQLiteDatabase database = this.getWritableDatabase();
           ContentValues values = new ContentValues();

           values.put(MySQLiteHelper.I_COLUMN_OBJECT_ID, objectId);
           values.put(MySQLiteHelper.I_COLUMN_OBJECTNAME, objectName);
           values.put(MySQLiteHelper.I_COLUMN_DESCRIPTION, description);
           values.put(MySQLiteHelper.I_COLUMN_LOCATION, location);
           values.put(MySQLiteHelper.I_COLUMN_CUSTOMERNAME, customerName);

           long insertId = database.insert(MySQLiteHelper.TABLE_INSPECTIONOBJECTS, null,
                   values);
       }

       //get all assignments from the database
       //returns a list with all assignmentNames
       public List<Assignment> getAllAssignments() {
           List<Assignment> listAssignments = new ArrayList<Assignment>();
           String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_ASSIGNMENTS;
           SQLiteDatabase db = this.getReadableDatabase();
           Cursor c = db.rawQuery(selectQuery, null);

           // looping through all rows and adding to list
           if (c.moveToFirst()) {
               do {
                   Assignment assignment = new Assignment();
                   assignment.setId(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENT_ID))));
                   assignment.setAssignmentName(c.getString((c.getColumnIndex(A_COLUMN_ASSIGNMENTNAME))));
                   assignment.setDescription((c.getString(c.getColumnIndex(A_COLUMN_DESCRIPTION))));
                   assignment.setStartDate((c.getInt(c.getColumnIndex(A_COLUMN_STARTDATE))));
                   assignment.setDueDate((c.getInt(c.getColumnIndex(A_COLUMN_ENDDATE))));
                   assignment.setIsTemplate((c.getString(c.getColumnIndex(A_COLUMN_ISTEMPLATE))));
                   // adding to assignment list
                   listAssignments.add(assignment);
               } while (c.moveToNext());
           }

           return listAssignments;
       }

       //get all userName from the local database
       //returns a list with all userNames
       public List<String> getAllUserNames() {
           List<String> listUserNames = new ArrayList<String>();
           String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_USERS;



           SQLiteDatabase db = this.getReadableDatabase();
           Cursor c = db.rawQuery(selectQuery, null);

           // looping through all rows and adding to list
           if (c.moveToFirst()) {
               do {
                   User user = new User();
                   user.setUserName(c.getString((c.getColumnIndex(U_COLUMN_USERNAME))));


                   // adding to assignment list
                   listUserNames.add(user.getUserName());
               } while (c.moveToNext());
           }

           return listUserNames;
       }

       // closing database
       public void closeDB() {
           SQLiteDatabase database = this.getReadableDatabase();
           if (database != null && database.isOpen())
               database.close();
       }

       }