package com.example.cmp309coursework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class database_helper extends SQLiteOpenHelper {
    private static final String TAG = "DB";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "HolyShip";
    private static final String TABLE_NAME = "HighScores";
    private static final String[] COLUMN_NAMES = {"ID", "Nickname", "Score"};
    private static final String[] COLUMN_TYPE = {"INTEGER", "STRING", "INTEGER"};
    private SQLiteDatabase db = this.getReadableDatabase();

    private static database_helper instance = null;
    private database_helper context = this;

    // Build a table creation query string
    public String createCreateString(){

        StringBuilder s = new StringBuilder("CREATE TABLE IF NOT EXISTS HighScores (");
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

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(createCreateString());
        } catch (Error e) {
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

        boolean empty = checkEmpty();

        if (empty == true) {
            Log.d(TAG, "Adding example data");

            String[] exampleNames = {"HMS Boaty McBoatface", "USS Enterprise", "USS Voyager", "HMS Interceptor", "HMS Lydia"};
            int[] exampleScores = {200, 50, 214, 3, 158};

            ContentValues row = new ContentValues();
            // Prepare a row for saving
            for(int newItems = 0; newItems != 5; newItems++)
            {
                row.put(COLUMN_NAMES[0], newItems);
                row.put(COLUMN_NAMES[1], exampleNames[newItems]);
                row.put(COLUMN_NAMES[2], exampleScores[newItems]);
                // Returns long, the row ID of the newly inserted row or, -1 if an error occurred
                if (db.insertOrThrow(TABLE_NAME, null, row) == -1)
                { Log.e(TAG, "Insert Method threw an error"); }
                else
                { Log.d(TAG,"Example data added"); }
                //row = new ContentValues();
            }
        }
        else
        {
            Log.d(TAG, "Data already exists, don't need to add an example");
        }
        db.close();
    }

    public boolean checkEmpty()
    {
        boolean empty = true;
        Cursor mcursor = db.rawQuery("SELECT COUNT(*) FROM HighScores", null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0)
        {
            empty = false;
        }

        return empty;

    }

    public void addHighScores(String prefix, String nickname, int score)
    {
        Log.d(TAG, "Adding scores");
        ContentValues row = new ContentValues();
        // Prepare a row for saving
        Random random = new Random();
        int id = random.nextInt(1000);

        row.put(COLUMN_NAMES[0], (id));
        row.put(COLUMN_NAMES[1], (prefix + " " + nickname));
        row.put(COLUMN_NAMES[2], score);

        // Returns long, the row ID of the newly inserted row or, -1 if an error occurred
        if (db.insertOrThrow(TABLE_NAME, null, row) == -1)
            {
                Log.e(TAG, "Insert Method threw an error");
            }
        else
            {
                Log.d(TAG,"High score data added");
            }
    }

    public String getScores(){

        // check the number of rows
        SQLiteDatabase db = this.getReadableDatabase();
        String output = "";

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

        } catch (Exception e)
        {
            Log.d(TAG, "This failed, no results?" + e);
            output = ("No results, stop looking at the scoreboard and play the bloody game");
        }
        return output;
    }

    public void clearDB()
    {
       db.execSQL("DELETE FROM "+ TABLE_NAME +";");
       db.execSQL("vacuum;");

        Log.d(TAG, "Table deleted");
    }


}