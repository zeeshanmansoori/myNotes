package com.example.myNotes.Asynctask;

import android.os.AsyncTask;

import com.example.myNotes.Room.NoteDao;

public class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
    private static final String TAG = "InsertAsyncTask";
    public NoteDao noteDao;
    public DeleteAllAsyncTask(NoteDao noteDao){
        this.noteDao = noteDao;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        noteDao.deleteAllNotes();
        return null;
    }
}
