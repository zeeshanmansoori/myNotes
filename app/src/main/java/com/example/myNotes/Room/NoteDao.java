package com.example.myNotes.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myNotes.Entity.Notes;

import java.util.List;

@Dao
public interface NoteDao{

    @Insert
    void insert(Notes notes);

    @Update
    void update(Notes notes);

    @Delete
    void delete(Notes note);

    @Query("SELECT * FROM note_table")
    LiveData<List<Notes>> getAllNotes();

    @Query("Delete FROM note_table")
    void deleteAllNotes();
}
