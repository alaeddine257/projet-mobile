package com.advaith.noteapp.database;

import android.provider.BaseColumns;


public final class NoteContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private NoteContract() {
    }

    /* Inner class that defines the table contents */
    public static class Notes implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TIME = "time_created";
    }
}
