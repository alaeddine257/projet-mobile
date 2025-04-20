package com.advaith.noteapp.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.advaith.noteapp.R;
import com.advaith.noteapp.database.NoteContract;
import com.advaith.noteapp.database.NoteDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

public class NewNoteActivity extends AppCompatActivity {

    NoteDbHelper dbHelper = new NoteDbHelper(this);

    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected boolean saveNote(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteContract.Notes.COLUMN_NAME_TITLE, title);
        values.put(NoteContract.Notes.COLUMN_NAME_CONTENT, content);
        values.put(NoteContract.Notes.COLUMN_NAME_TIME, Calendar.getInstance().getTimeInMillis());

        long newRowId = db.insert(NoteContract.Notes.TABLE_NAME, null, values);
        return newRowId != -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(actionBar).setTitle(R.string.new_note_activity_toolbar);


        final EditText titleEditText, contentEditText;
        titleEditText = findViewById(R.id.noteTitle);
        contentEditText = findViewById(R.id.noteContent);
        FloatingActionButton doneButton = findViewById(R.id.saveNoteButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                String title, content;
                boolean noteValid = true;
                title = titleEditText.getText().toString();
                content = contentEditText.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(NewNoteActivity.this, R.string.empty_title, Toast.LENGTH_SHORT).show();
                    noteValid = false;
                }
                if (content.isEmpty()) {
                    Toast.makeText(NewNoteActivity.this, R.string.empty_content, Toast.LENGTH_SHORT).show();
                    noteValid = false;
                }
                if (noteValid && saveNote(title, content)) {
                    finish();
                    Toast.makeText(NewNoteActivity.this, R.string.note_save_successful, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
