package com.example.myNotes.Utils;

import com.example.myNotes.Entity.Notes;

import java.util.ArrayList;

public class MainStateManager {

    private ArrayList<Notes> selectedNotesList= new ArrayList<>();

    public void AddNote(Notes notes){
        selectedNotesList.add(notes);
    }
    public void removeNote(Notes notes) {
        selectedNotesList.remove(notes);
    }
    public boolean isSelected(Notes notes) {
        return selectedNotesList.contains(notes);
    }

}
