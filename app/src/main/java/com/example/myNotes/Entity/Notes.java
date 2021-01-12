package com.example.myNotes.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Notes implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "charachters")
    private String charachters;

    @ColumnInfo(name = "timestamp")
    private String timestamp;


    public Notes(String title, String content, String charachters, String timestamp) {
        this.title = title;
        this.content = content;
        this.charachters = charachters;
        this.timestamp = timestamp;
    }


    protected Notes(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        charachters = in.readString();
        timestamp = in.readString();

    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharachters() {
        return charachters;
    }

    public void setCharachters(String charachters) {
        this.charachters = charachters;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(charachters);
        dest.writeString(timestamp);
    }
}
