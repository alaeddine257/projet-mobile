package com.advaith.noteapp.utils;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.advaith.noteapp.R;
import com.advaith.noteapp.activities.ViewNoteActivity;
import com.advaith.noteapp.database.NoteContract;
import com.advaith.noteapp.database.NoteDbHelper;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private NoteDbHelper dbHelper;

    public RecyclerAdapter(NoteDbHelper noteDbHelper) {
        super();
        this.dbHelper = noteDbHelper;
        this.noteList = extractNotes();
    }

    private List<Note> extractNotes() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + NoteContract.Notes.TABLE_NAME + " ORDER BY " + NoteContract.Notes.COLUMN_NAME_TIME + " DESC",
                null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(NoteContract.Notes._ID));
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(NoteContract.Notes.COLUMN_NAME_TITLE));
            String content = cursor.getString(
                    cursor.getColumnIndexOrThrow(NoteContract.Notes.COLUMN_NAME_CONTENT));
            notes.add(new Note(id, title, content));
        }
        cursor.close();
        return notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.notelist_item, parent, false);
        return new NoteViewHolder(cardView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        TextView textView = holder.cardView.findViewById(R.id.noteTitleDisplay);
        textView.setText(noteList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final RecyclerAdapter recyclerAdapter;
        CardView cardView;

        NoteViewHolder(@NonNull CardView itemView, RecyclerAdapter recyclerAdapter) {
            super(itemView);
            cardView = itemView.findViewById(R.id.devCardView);
            this.recyclerAdapter = recyclerAdapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Note clickedNote = recyclerAdapter.noteList.get(position);
            Intent intent = new Intent(view.getContext(), ViewNoteActivity.class);
            intent.putExtra("clickedNote", clickedNote);
            view.getContext().startActivity(intent);
        }
    }
}
