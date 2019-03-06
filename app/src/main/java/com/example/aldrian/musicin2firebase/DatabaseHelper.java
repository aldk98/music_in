package com.example.aldrian.musicin2firebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aldrian.musicin2firebase.model.Job;
import com.example.aldrian.musicin2firebase.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tommy on 7/12/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 13;

    // Database Name
    private static final String DATABASE_NAME = "MusicIn.db";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //---------------------------------------------//

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ROLE = "user_role";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PHONE = "user_phone";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private String[] ALL_USER_COLUMNS={
            COLUMN_USER_ID,
            COLUMN_USER_ROLE,
            COLUMN_USER_EMAIL,
            COLUMN_USER_PHONE,
            COLUMN_USER_NAME,
            COLUMN_USER_PASSWORD
    };

    //---------------------------------------------//

    private static final String TABLE_JOB ="job";

    // Jobs table definition
    private static final String COLUMN_JOB_ID="job_id";
    private static final String COLUMN_JOB_BUSINESS_NAME="job_businessName";
    private static final String COLUMN_JOB_ADDRESS="job_address";
    private static final String COLUMN_JOB_TIME="job_time";
    private static final String COLUMN_JOB_PAYING_RANGE="job_paying_range";
    private static final String COLUMN_JOB_DATE="job_date";
    private static final String COLUMN_JOB_GENRE="job_genre";
    private static final String COLUMN_JOB_STATUS="job_status";
    private static final String COLUMN_MUSICIAN_ID="musician_id";
    private static final String COLUMN_OWNER_ID="owner_id";

    private String[] ALL_JOB_COLUMNS={
            COLUMN_JOB_ID,
            COLUMN_JOB_BUSINESS_NAME,
            COLUMN_JOB_ADDRESS,
            COLUMN_JOB_TIME,
            COLUMN_JOB_PAYING_RANGE,
            COLUMN_JOB_TIME,
            COLUMN_JOB_DATE,
            COLUMN_JOB_GENRE,
            COLUMN_JOB_STATUS,
            COLUMN_MUSICIAN_ID,
            COLUMN_OWNER_ID
    };

    //STATUS ID
    private final static String ON_APPEAL="on_appeal";
    private final static String ACCEPTED="accepted";
    private final static String DENIED="denied";
    private final static String CREATED="created";


    //---------------------------------------------//
    // create table sql query

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ROLE + " TEXT,"
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PHONE + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_JOB_TABLE =
            "CREATE TABLE job (" +
                    "job_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "job_businessName TEXT," +
                    "job_address TEXT," +
                    "job_time TEXT, " +
                    "job_paying_range TEXT, " +
                    "job_date TEXT, " +
                    "job_genre TEXT," +
                    "job_status TEXT," +
                    "musician_id INTEGER," +
                    "owner_id INTEGER," +
                    "FOREIGN KEY (musician_id) REFERENCES user (user_id)," +
                    "FOREIGN KEY (owner_id) REFERENCES user (user_id))";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_JOB_TABLE = "DROP TABLE IF EXISTS job";

    //---------------------------------------------//

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_JOB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_JOB_TABLE);
        // Create tables again
        onCreate(db);
    }

    //---------------------------------------------//

    // CREATE
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ROLE, user.getRole());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public long addJob(Job job){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("job_businessName", job.getBusinessName());
        values.put("job_address", job.getAddress());
        values.put("job_time", job.getTime());
        values.put(COLUMN_JOB_PAYING_RANGE, job.getPayRange());
        values.put(COLUMN_JOB_DATE, job.getDate());
        values.put(COLUMN_JOB_GENRE, job.getGenre());
        values.put(COLUMN_JOB_STATUS,CREATED);
        values.put(COLUMN_MUSICIAN_ID, "null");
        values.put(COLUMN_OWNER_ID, job.getOwner_id());

        long id=db.insert("job", null, values);
        db.close();
        Log.d(" ADDED JOB OWNER ID",String.valueOf(job.getOwner_id()));
        return id;
    }

    //---------------------------------------------//

    //READ one USER
    public User getUser(String email,String password){
        User user= new User();
        String[] columns = ALL_USER_COLUMNS;
        String selection=COLUMN_USER_EMAIL+" = ?"
                +" AND "+COLUMN_USER_PASSWORD+" = ?";
        String[] selectionArgs={email,password};
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.query(
                TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        if(cursor.getCount()>0){
//            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));

            cursor.close();
            db.close();
            return user;
        }
        return null;
    }

    //READ one USER by musician_id
    public User getUser(String musician_id){
        User user = new User();
        String[] columns = ALL_USER_COLUMNS;
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(musician_id)};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
//                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE)));

                cursor.close();
                db.close();
//                Log.d("MUSICIAN id ditemukan:",String.valueOf(user.getId()));
            }

            return user;
        }
        return null;
    }

    // READ
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_ROLE,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PHONE,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder = COLUMN_USER_NAME + " ASC";

        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
//                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setRole(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROLE)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));

                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    //---------------------------------------------//

    //GET ALL ON APPEAL JOB
    public List<Job> getOnAppeal(String owner_id) {
//        Log.d("USER ID",String.valueOf(getUser(owner_id).getId()));
        Log.d("USER ROLE",String.valueOf(getUser(owner_id).getRole()));
        SQLiteDatabase db = this.getReadableDatabase();
        List<Job> listJob = new ArrayList<>();
        String[] columns = ALL_JOB_COLUMNS;
        String selection = COLUMN_JOB_STATUS + " = ?"
                +" AND "+COLUMN_OWNER_ID+" = ?";
        String[] selectionArgs = {ON_APPEAL,String.valueOf(owner_id)};
        Cursor cursor =db.query(
                TABLE_JOB,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Job job = new Job();
                    job.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ID)));
                    job.setMusician_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSICIAN_ID)));
                    job.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_TIME)));
                    job.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ADDRESS)));
                    job.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_DATE)));
                    listJob.add(job);
                }
            }
            return listJob;
        }
        cursor.close();
        db.close();
        return null;
    }

    //GET ALL ACCEPTED JOB
    public List<Job> getMusicianAcceptedJob(int musician_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Job> listJob= new ArrayList<>();
        String[] columns = ALL_JOB_COLUMNS;
        String selection = COLUMN_JOB_STATUS + " = ?"
                +" AND " + COLUMN_MUSICIAN_ID + " = ?";
        String[] selectionArgs = {ACCEPTED,String.valueOf(musician_id)};
        Cursor cursor = db.query(
                TABLE_JOB,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Job job = new Job();
                    job.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ID)));
                    job.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ADDRESS)));
                    job.setBusinessName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_BUSINESS_NAME)));
                    job.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_DATE)));
                    job.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_GENRE)));
                    job.setPayRange(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_PAYING_RANGE)));
                    job.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_TIME)));
                    job.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_STATUS)));
                    job.setMusician_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSICIAN_ID)));
                    job.setOwner_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER_ID)));

                    listJob.add(job);
                    return listJob;
                }
            }
        }
        cursor.close();
        db.close();
        return null;
    }

    public List<Job> getOwnerAcceptedJob(int owner_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Job> listJob= new ArrayList<>();
        String[] columns = ALL_JOB_COLUMNS;
        String selection = COLUMN_JOB_STATUS + " = ?"
                +" AND " + COLUMN_OWNER_ID + " = ?";
        String[] selectionArgs = {ACCEPTED,String.valueOf(owner_id)};
        Cursor cursor = db.query(
                TABLE_JOB,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Job job = new Job();
                    job.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ID)));
                    job.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ADDRESS)));
                    job.setBusinessName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_BUSINESS_NAME)));
                    job.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_DATE)));
                    job.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_GENRE)));
                    job.setPayRange(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_PAYING_RANGE)));
                    job.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_TIME)));
                    job.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_STATUS)));
                    job.setMusician_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSICIAN_ID)));
                    job.setOwner_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER_ID)));

                    listJob.add(job);
                    return listJob;
                }
            }
        }
        cursor.close();
        db.close();
        return null;
    }

    //GET ALL DENIED JOBS
    public List<Job> getDeniedJob(int musician_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Job> listJob= new ArrayList<>();
        String[] columns = ALL_JOB_COLUMNS;
        String selection =COLUMN_JOB_STATUS+" = ?"
                +" AND "+COLUMN_MUSICIAN_ID+" = ?";
        String[] selectionArgs={DENIED,String.valueOf(musician_id)};
        Cursor cursor =db.query(
                TABLE_JOB,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Job job=new Job();
                job.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ID)));
                job.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ADDRESS)));
                job.setBusinessName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_BUSINESS_NAME)));
                job.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_DATE)));
                job.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_GENRE)));
                job.setPayRange(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_PAYING_RANGE)));
                job.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_TIME)));
                job.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_STATUS)));
                job.setMusician_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSICIAN_ID)));
                job.setOwner_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER_ID)));
                listJob.add(job);
            }
        }
        cursor.close();
        db.close();
        return listJob;
    }

    //GET ALL CREATED JOBS for musician's find job
    public List<Job> getCreatedJob(){
        String[] columns=ALL_JOB_COLUMNS;

        String sortOrder = "job_date ASC";

        String selection=COLUMN_JOB_STATUS+" = ?";
        String[] selectionArgs={CREATED};

        List<Job> jobList = new ArrayList<Job>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("job",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        if(cursor.moveToFirst()){
            do{
                Job job = new Job();
                job.setId(cursor.getString(cursor.getColumnIndex("job_id")));
                job.setBusinessName(cursor.getString(cursor.getColumnIndex("job_businessName")));
                job.setAddress(cursor.getString(cursor.getColumnIndex("job_address")));
                job.setTime(cursor.getString(cursor.getColumnIndex("job_time")));
                job.setPayRange(cursor.getString(cursor.getColumnIndex("job_paying_range")));
                job.setDate(cursor.getString(cursor.getColumnIndex("job_date")));
                job.setGenre(cursor.getString(cursor.getColumnIndex("job_genre")));
                job.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_JOB_STATUS)));

                jobList.add(job);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return jobList;
    }

    //get Jobs with owner's id
    public List<Job> getCreatedJob(int id){
        String[] columns=ALL_JOB_COLUMNS;

        String sortOrder = "job_date ASC";

        String selection=COLUMN_JOB_STATUS+" = ?"
                +" AND "+COLUMN_OWNER_ID+" = ?";
        String[] selectionArgs={CREATED,String.valueOf(id)};

        List<Job> jobList = new ArrayList<Job>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("job",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()) {
                do {
                    Job job = new Job();
                    job.setId(cursor.getString(cursor.getColumnIndex("job_id")));
                    job.setBusinessName(cursor.getString(cursor.getColumnIndex("job_businessName")));
                    job.setAddress(cursor.getString(cursor.getColumnIndex("job_address")));
                    job.setTime(cursor.getString(cursor.getColumnIndex("job_time")));
                    job.setPayRange(cursor.getString(cursor.getColumnIndex("job_paying_range")));
                    job.setDate(cursor.getString(cursor.getColumnIndex("job_date")));
                    job.setGenre(cursor.getString(cursor.getColumnIndex("job_genre")));
                    job.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_JOB_STATUS)));
                    job.setOwner_id(cursor.getString(cursor.getColumnIndex(COLUMN_OWNER_ID)));
                    jobList.add(job);
                } while (cursor.moveToNext());

            }
            return jobList;
        }
        cursor.close();
        db.close();

        return null;
    }

    //---------------------------------------------//

    //UPDATE JOB
    public void appealJob(int job_id, int musician_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("JOB OWNER ID",String.valueOf(getJob(job_id).getOwner_id()));
        ContentValues values = new ContentValues();
        values.put(COLUMN_MUSICIAN_ID, musician_id);
        values.put(COLUMN_JOB_STATUS, ON_APPEAL);
        String whereClause=COLUMN_JOB_ID+" = ?"
                +" AND "+COLUMN_JOB_STATUS+" = ?";

        String[] whereArgs={String.valueOf(job_id),CREATED};
        db.close();
        db=this.getWritableDatabase();
        // updating row
        db.update(TABLE_JOB,
                values,
                whereClause,
                whereArgs);
        db.close();
        Log.d("Job ID",String.valueOf(job_id));
        Log.d("musician ID",String.valueOf(musician_id));
        Log.d("JOB status",getJob(job_id).getStatus());
        Log.d("Job Owner",String.valueOf(getJob(job_id).getOwner_id()));
    }

    public void acceptJob(int job_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_JOB_ID, job_id);
        values.put(COLUMN_JOB_STATUS, ACCEPTED);

        // updating row
        db.update(TABLE_JOB, values, COLUMN_JOB_ID + " = ?",
                new String[]{String.valueOf(job_id)});
        db.close();
    }

    public void denyJob(int job_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_JOB_ID, job_id);
        values.put(COLUMN_JOB_STATUS, DENIED);

        // updating row
        db.update(TABLE_JOB, values, COLUMN_JOB_ID + " = ?",
                new String[]{String.valueOf(job_id)});
        db.close();
    }

    public Job getJob(int job_id){
        Job job = new Job();
        SQLiteDatabase mdb=this.getReadableDatabase();
        String[] columns=ALL_JOB_COLUMNS;
        String selection=COLUMN_JOB_ID+" = ?";
        String[] selectionArgs= {String.valueOf(job_id)};
        Cursor cursor=mdb.query(
                TABLE_JOB,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int cursorCount=cursor.getCount();
        cursor.moveToFirst();
        if(cursorCount>0){
            job.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ID)));
            job.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_ADDRESS)));
            job.setBusinessName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_BUSINESS_NAME)));
            job.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_DATE)));
            job.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_GENRE)));
            job.setPayRange(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_PAYING_RANGE)));
            job.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_TIME)));
            job.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JOB_STATUS)));
            job.setMusician_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MUSICIAN_ID)));
            job.setOwner_id(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER_ID)));
            Log.d("LOG",job.getStatus());

            cursor.close();
            mdb.close();
            return job;
        }
        cursor.close();
        mdb.close();
        return null;
    }

    //---------------------------------------------//

    // UPDATE
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ROLE, user.getRole());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
//        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
//                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // DELETE
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
//        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
//                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //---------------------------------------------//

    // CHECK EMAIL
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    // CHECK EMAIL & PASSWORD
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" +
                " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkRoleMusician(String email){

        SQLiteDatabase db =this.getReadableDatabase();
        String[] columns = {
                COLUMN_USER_ID
        };
        String selection =COLUMN_USER_EMAIL+" = ?"
                +" AND "+COLUMN_USER_ROLE+" = ?";
        String selectionArgs[]= {email,"musician"};
        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null

        );
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
                if(cursorCount>0){
                     return true;
                }
        return false;
    }


}
