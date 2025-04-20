package com.advaith.noteapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notes.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + NoteContract.Notes.TABLE_NAME + " (" +
                    NoteContract.Notes._ID + " INTEGER PRIMARY KEY," +
                    NoteContract.Notes.COLUMN_NAME_TITLE + " TEXT," +
                    NoteContract.Notes.COLUMN_NAME_CONTENT + " TEXT," +
                    NoteContract.Notes.COLUMN_NAME_TIME + " TIME)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteContract.Notes.TABLE_NAME;

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
