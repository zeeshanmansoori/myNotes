package com.example.myNotes.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.myNotes.Entity.Notes;
import com.example.myNotes.Repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private LiveData<List<Notes>> allNotes;
    private NoteRepository repository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();

    }

    public void insert(Notes note) {
        repository.insert(note);
    }

    public void update(Notes note) {
        repository.update(note);
    }

    public void delete(Notes note) {
        repository.delete(note);
    }

    public LiveData<List<Notes>> getAllNotes() {
        return allNotes;
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

}
