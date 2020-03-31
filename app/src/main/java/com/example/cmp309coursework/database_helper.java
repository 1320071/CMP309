package com.example.cmp309coursework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
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

    private database_helper(Context context){
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

    // Saves a single text string to SQLite database, overwriting existing one in row 0
//    public void saveText(String text){
//        // Check the number of rows
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Get all table rows
//        Cursor result = db.query(TABLE_NAME, COLUMN_NAMES, null, null, null, null, null, null);
//        int count = 0;
//        if(result != null) {
//            // See how many entries there are
//            count = result.getCount();
//            db.close();
//            db = getWritableDatabase();
//            ContentValues row = new ContentValues();
//            // Prepare a row for saving, use 0 as the ID
//            row.put(COLUMN_NAMES[0], 0);
//            row.put(COLUMN_NAMES[1], text);
//            // if no rows, INSERT one
//            if(count == 0){
//                db.insert(TABLE_NAME, null, row);
//            } else { // else update existing one
//                db.update(TABLE_NAME, row, "ID = '0'", null);
//            }
//            db.close();
//        }
//    }

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

    public String loadText(){
        // check the number of rows
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(TABLE_NAME, COLUMN_NAMES, null, null, null, null, null, null);
        int count = 0;

        // if there is text to load, return it, otherwise return error message
        if(result.getCount() > 0){
            result.moveToPosition(0);
            return result.getString(1);
        } else {
            return "NO TEXT STORED IN DB!";
        }
    }
}