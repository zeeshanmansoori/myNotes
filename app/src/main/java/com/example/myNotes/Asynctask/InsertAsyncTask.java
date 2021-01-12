package com.example.myNotes.Asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myNotes.Entity.Notes;
import com.example.myNotes.Room.NoteDao;

public class InsertAsyncTask extends AsyncTask<Notes,Void,Void> {
    private static final String TAG = "InsertAsyncTask";
    public NoteDao noteDao;
    public InsertAsyncTask(NoteDao noteDao){
        this.noteDao = noteDao;
    }
    @Override
    protected Void doInBackground(Notes... note) {
        Log.d(TAG, "doInBackground: inserted");
        noteDao.insert(note[0]);

        return null;
    }
}
