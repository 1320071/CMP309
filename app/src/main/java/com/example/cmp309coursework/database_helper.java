package com.example.cmp309coursework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class database_helper extends SQLiteOpenHelper {
    private static final String TAG = "DB";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HolyShip";
    private static final String TABLE_NAME = "HighScores";
    private static final String[] COLUMN_NAMES = {"ID", "Nickname", "Score"};
    private static final String[] COLUMN_TYPE = {"INTEGER", "STRING", "INTEGER"};

    private static database_helper instance = null;

    // Build a table creation query string
    public String createCreateString(){
        StringBuilder s = new StringBuilder("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (");
        for (int i = 0; i < database_helper.COLUMN_NAMES.length; i++) {
            s.append(database_helper.COLUMN_NAMES[i])
                    .append(" ")
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

//    public static database_helper getInstance(Context context){
//        if(instance == null){
//            instance = new database_helper(context);
//        }
//        return instance;
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(createCreateString());
        }catch(Error e) {
            Log.e(TAG, "Error creating table" + e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void addExampleData() {
        // Check the number of rows
        SQLiteDatabase db = getWritableDatabase();
        String[] nickname = {"Nickname"};
        Cursor getAll = db.rawQuery("SELECT * FROM HighScores", null);
        int count = 0;

        if (getAll.moveToFirst())
        {
            count = getAll.getInt(0);
            getAll.close();
        }


        if (count == 0) {
            Log.d(TAG, "Adding example data");
            int newItems = 0;

            //String[] exampleNames = {"HMS Boaty McBoatface", "USS Enterprise", "USS Voyager", "HMS Interceptor", "HMS Lydia"};
            //int[] exampleScores = {200, 50, 214, 3, 158};

            ContentValues row = new ContentValues();
            // Prepare a row for saving, use 0 as the ID
            row.put(COLUMN_NAMES[0], 0);
            row.put(COLUMN_NAMES[1], "HMS Boaty McBoatface");
            row.put(COLUMN_NAMES[2], 2000);


            // Returns long, the row ID of the newly inserted row or, -1 if an error occurred
            if (db.insertOrThrow(TABLE_NAME, null, row) == -1)
            { Log.e(TAG, "Insert Method threw an error"); }
            else
            { Log.d(TAG,"Example data added"); }
        }
        else
        {
            Log.d(TAG, "Data already exists, don't need to add an example");
        }
        db.close();
    }

    public String getScores(){
        // TODO make this a on debug
        addExampleData();

        // check the number of rows
        SQLiteDatabase db = this.getReadableDatabase();
        String output = "";

        //Cursor result = db.rawQuery("SELECT Nickname, Score FROM HighScores", null);
        //Log.d(TAG, "Querying for Nickname and score");


        // if there is text to load, return it, otherwise return error message
        try {
            Cursor result = db.rawQuery("SELECT Nickname, Score FROM HighScores ORDER BY Score DESC LIMIT 25;", null);
            Log.d(TAG, "Querying for Nickname and score");
            result.moveToFirst();

            int count2 = result.getInt(0);
            String[] scoresText = new String[0];
            int itemsLeft = result.getCount();
            int max = 25;

            Log.d(TAG, "results = " + result.getCount());
            Log.d(TAG, "There's results");

            while (itemsLeft != 0 && max != 0)
            {
                output = output + result.getString(0) + " " + result.getInt(1) + "\n";
                Log.d(TAG, "Output = " + output);
                result.moveToNext();
                itemsLeft--;
                max--;
            }

            result.close();

        } catch (Exception e) {
            Log.d(TAG, "This failed, no results?" + e);
            output = ("No results, stop looking at the scoreboard and play the bloody game");
        }
        return output;
    }
}