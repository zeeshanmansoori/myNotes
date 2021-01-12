package com.example.myNotes.Utils;

import com.example.myNotes.Entity.Notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MyUtil {

    public static String TimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("h:mm a , EEE MMM d");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public static Boolean HasChanged(Notes notes, String title, String content) {
        if (notes.getTitle().trim().equals(title) && notes.getContent().trim().equals(content)) {
            return false;
        }
        return true;
    }

    public static int Counter(String string) {
        string.trim();
        string = string.replaceAll("\\s+","");
        return string.length();
    }
}
