package com.yodaapp.live;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String KEY_USID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_EMAIL_REAL = "email_real";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_STATUS = "status";
    public static final String KEY_SNAME = "schoolname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_USER = "name";
    public static final String KEY_USER_TYPE = "user_type";

    private static final String DATABASE_NAME = "school";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "user_master";

    private static final String DATABASE_TABLE1 = "CREATE TABLE " + DATABASE_TABLE + " (" +
            KEY_USID + " INTEGER NOT NULL, " +
            KEY_SCHOOL_ID + " INTEGER NOT NULL, " +
            KEY_USER + " TEXT NOT NULL, " +
            KEY_SNAME + " TEXT NOT NULL, " +
            KEY_PHONE + " TEXT NOT NULL, " +
            KEY_EMAIL + " TEXT NOT NULL, " +
            KEY_EMAIL_REAL + " TEXT NOT NULL, " +
            KEY_PASSWORD + " TEXT NOT NULL, " +
            KEY_USER_TYPE + " TEXT NOT NULL, " +
            KEY_STATUS + " TINYINT NOT NULL DEFAULT 0);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(DATABASE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }


}