package com.example.myNotes.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myNotes.Entity.Notes;

@Database(entities = {Notes.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public static NoteDatabase instance;

    public abstract NoteDao getNoteDao();

    public synchronized static NoteDatabase getNoteDatabase(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext()
                    ,NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
//            Notes note = new Notes("title","desc","content");
//            for (int i = 0; i <5 ; i++) {
//                new InsertAsyncTask(instance.getNoteDao()).execute(note);
//            }

        }
    };
}
