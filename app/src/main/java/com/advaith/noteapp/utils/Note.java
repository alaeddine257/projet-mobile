package com.advaith.noteapp.utils;

import java.io.Serializable;

/**
 * Note is the class which holds the id, title and content of single note.
 * <p>
 * Whenever we read data from database, we initialise a Note object for each
 * note read from the database. This way, the notes are encapsulated.
 */

public class Note implements Serializable {
    private int id;
    private String title;
    private String content;


    Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
