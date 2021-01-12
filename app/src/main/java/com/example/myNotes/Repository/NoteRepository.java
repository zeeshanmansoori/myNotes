package com.example.myNotes.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myNotes.Asynctask.DeleteAllAsyncTask;
import com.example.myNotes.Asynctask.DeleteAsyncTask;
import com.example.myNotes.Asynctask.InsertAsyncTask;
import com.example.myNotes.Asynctask.UpdateAsyncTask;
import com.example.myNotes.Entity.Notes;
import com.example.myNotes.Room.NoteDao;
import com.example.myNotes.Room.NoteDatabase;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Notes>> allNotes;
    private NoteDatabase  database;

    public NoteRepository(Application application){
        database = NoteDatabase.getNoteDatabase(application);
        noteDao = database.getNoteDao();
        allNotes = noteDao.getAllNotes();

    }

    public void insert(Notes note){
        new InsertAsyncTask(noteDao).execute(note);
    }

    public void update(Notes note){
        new UpdateAsyncTask(noteDao).execute(note);
    }
    public void delete(Notes note){
        new DeleteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes(){
        new DeleteAllAsyncTask(noteDao).execute();
    }
    public LiveData<List<Notes>> getAllNotes(){
        return allNotes;
    }
}
