package com.example.cmp309coursework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

public class database_helper extends SQLiteOpenHelper {
    private static final String TAG = "Menu";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HolyShip";
    private static final String TABLE_NAME = "HighScores";
    private static final String[] COLUMN_NAMES = {"ID", "Nickname", "Score"};
    private static final String[] COLUMN_TYPE = {"int", "string", "int"};

    private static database_helper instance = null;

    // Build a table creation query string
    private String createCreateString(){
        StringBuilder s = new StringBuilder("CREATE TABLE " + TABLE_NAME + " (");

        for (int i = 0; i < database_helper.COLUMN_NAMES.length; i++) {
            s.append(database_helper.COLUMN_NAMES[i])
                    .append(database_helper.COLUMN_TYPE[i]);

            if(i < database_helper.COLUMN_NAMES.length - 1){
                s.append(", ");
            } else {
                s.append(");");
            }
        }
        return s.toString();
    }

    public database_helper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static database_helper getInstance(Context context){
        if(instance == null){
            instance = new database_helper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createCreateString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void addExampleData() {
        // Check the number of rows
        SQLiteDatabase db = getWritableDatabase();

        ContentValues row = new ContentValues();
        // Prepare a row for saving, use 0 as the ID
        row.put(COLUMN_NAMES[0], 0);
        row.put(COLUMN_NAMES[1], "Dave Luntt");
        row.put(COLUMN_NAMES[1], 2000);

        // Returns long, the row ID of the newly inserted row or, -1 if an error occurred
        if (db.insertOrThrow(TABLE_NAME, null, row) == -1)
            { Log.e("DB", "Insert Method threw an error"); }
        else
            { Log.d("DB","Example data added"); };


        db.close();
    }

    public String getScores(){
        // check the number of rows
        createCreateString();
        addExampleData();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] displayColumns = {"Nickname", "Score"};

        Cursor result = db.query(TABLE_NAME, displayColumns, null, null, null, null, null, null);
        int count = 0;
        db.close();
        // if there is text to load, return it, otherwise return error message
        if(result.getCount() > 0){
            result.moveToPosition(0);
            return (result.getString(1) + "\n");

        } else {
            return "No high scores!";
        }
    }
}