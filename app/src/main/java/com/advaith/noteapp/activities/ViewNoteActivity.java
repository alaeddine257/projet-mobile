package com.advaith.noteapp.activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.advaith.noteapp.R;
import com.advaith.noteapp.database.NoteContract;
import com.advaith.noteapp.database.NoteDbHelper;
import com.advaith.noteapp.utils.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class ViewNoteActivity extends AppCompatActivity {

    private static final boolean VIEW_MODE = true;
    private static final boolean EDIT_MODE = false;

    SQLiteDatabase db;
    private NoteDbHelper dbHelper = new NoteDbHelper(this);
    private Note note;
    private FloatingActionButton saveNoteFab;
    private boolean noteMode;

    protected boolean deleteNote() {
        String selection = "_id = ? ";
        String[] selectionArgs = {String.valueOf(note.getId())};

        int deletedRows = db.delete(
                NoteContract.Notes.TABLE_NAME,
                selection,
                selectionArgs
        );
        return deletedRows == 1;
    }

    protected boolean saveEdits() {
        EditText editNoteTitle = findViewById(R.id.noteEditTitle);
        EditText editNoteContent = findViewById(R.id.noteEditContent);

        String newTitle = editNoteTitle.getText().toString();
        String newContent = editNoteContent.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.Notes.COLUMN_NAME_TITLE, newTitle);
        contentValues.put(NoteContract.Notes.COLUMN_NAME_CONTENT, newContent);

        String selection = "_id = ? ";
        String[] selectionArgs = {String.valueOf(note.getId())};

        int editedRows = db.update(
                NoteContract.Notes.TABLE_NAME,
                contentValues,
                selection,
                selectionArgs
        );
        return editedRows == 1;
    }

    @SuppressLint("RestrictedApi")
    protected void startEditing() {
        ViewSwitcher viewSwitcherTitle = findViewById(R.id.viewSwitcherTitle);
        ViewSwitcher viewSwitcherContent = findViewById(R.id.viewSwitcherContent);

        EditText editNoteTitle = findViewById(R.id.noteEditTitle);
        TextView noteViewTitle = findViewById(R.id.noteViewTitle);
        EditText editNoteContent = findViewById(R.id.noteEditContent);
        TextView noteViewContent = findViewById(R.id.noteViewContent);

        CharSequence noteTitle = noteViewTitle.getText();
        CharSequence noteContent = noteViewContent.getText();

        editNoteTitle.setText(noteTitle);
        editNoteContent.setText(noteContent);

        viewSwitcherTitle.showNext();
        viewSwitcherContent.showNext();
        saveNoteFab.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.view_note_activity_toolbar);


        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("clickedNote");
        noteMode = ViewNoteActivity.VIEW_MODE;

        TextView noteViewTitle = findViewById(R.id.noteViewTitle);
        TextView noteViewContent = findViewById(R.id.noteViewContent);
        noteViewTitle.setText(note.getTitle());
        noteViewContent.setText(note.getContent());
        db = dbHelper.getWritableDatabase();

        saveNoteFab = findViewById(R.id.saveEditedNoteFab);
        saveNoteFab.setVisibility(View.INVISIBLE);
        saveNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveEdits()) {
                    finish();
                    Toast.makeText(ViewNoteActivity.this, R.string.note_edit_successful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewNoteActivity.this, R.string.note_edit_unsuccessful, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_note_alert_title);
            builder.setMessage(R.string.delete_note_alert_content);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (deleteNote()) {
                        finish();
                        Toast.makeText(ViewNoteActivity.this, R.string.note_delete_successful, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewNoteActivity.this, R.string.note_delete_unsuccessful, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ViewNoteActivity.this, R.string.note_delete_cancelled, Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
            return true;
        } else if (id == R.id.action_edit) {
            ActionBar toolbar = getSupportActionBar();
            if (noteMode == ViewNoteActivity.VIEW_MODE) {
                Objects.requireNonNull(toolbar).setTitle(getString(R.string.edit_note_activity_toolbar));
                noteMode = ViewNoteActivity.EDIT_MODE;
            } else {
                Objects.requireNonNull(toolbar).setTitle(getString(R.string.view_note_activity_toolbar));
                noteMode = ViewNoteActivity.VIEW_MODE;
            }
            startEditing();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
