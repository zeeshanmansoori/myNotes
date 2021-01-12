package com.example.myNotes.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myNotes.Entity.Notes;
import com.example.myNotes.R;
import com.example.myNotes.Utils.MyUtil;

import static com.example.myNotes.Ui.MainActivity.STRING_EXRTRA;

public class NoteActivity extends AppCompatActivity {

    public static final String MYNOTE = "sendNote" ;
    public static final int DELETECODE = 30;
    //views
    TextView charachters,timestamp;
    AppCompatEditText title;
    EditText content;
    Toolbar toolbar;

    Intent intent;
    Boolean isEditModeEnabled = false,isNewNote = false;
    String mTitle,mContent,mTimeStamp,mCharacters;
    Notes note;
    GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (!isEditModeEnabled)
                    enableEditable();
                else disableEditable();
                return super.onDoubleTap(e);
            }
        });
        findingIds();
        setActionbarProp();
        setListeners();
        getMyIntent();

    }

    private void setActionbarProp() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners(){
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charachters.setText("characters "+String.valueOf(MyUtil.Counter(content.getText().toString())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });


    }

    private void findingIds() {
        timestamp = findViewById(R.id.timestamp);
        charachters = findViewById(R.id.count);
        title = findViewById(R.id.title);
        content = findViewById(R.id.edittext);
        toolbar = findViewById(R.id.toolbar);
    }


    private void getMyIntent() {
        intent = getIntent();
        switch (intent.getStringExtra(STRING_EXRTRA)){
            case "Ui.MainActivity":
                enableEditable();
                isNewNote = true;
                break;
            case "MyAdapter":
                disableEditable();
                isNewNote = false;
                break;
        }
        setProp();
    }

    private void disableEditable() {
        isEditModeEnabled = false;
        content.setFocusable(false);
        content.setCursorVisible(false);
        content.setInputType(InputType.TYPE_CLASS_TEXT);
        content.setFocusableInTouchMode(false);
        content.setSingleLine(false);
        title.setFocusable(false);
        title.setCursorVisible(false);
        title.setInputType(EditText.AUTOFILL_TYPE_TEXT);
        title.setFocusableInTouchMode(false);
        hideKeyBoard(content);
    }

    private void enableEditable() {
        isEditModeEnabled = true;
        content.setFocusable(true);
        content.setCursorVisible(true);
        content.setInputType(InputType.TYPE_CLASS_TEXT);
        content.setFocusableInTouchMode(true);
        content.requestFocus();
        content.setSingleLine(false);
        title.setFocusable(true);
        title.setCursorVisible(true);
        title.setInputType(EditText.AUTOFILL_TYPE_TEXT);
        title.setFocusableInTouchMode(true);
        showKeyBoard(content);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu,menu);
        return true;
        // true = want to display the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.share:

                shareIntent();
                return true;
            case R.id.hide:
                Toast.makeText(this, "hide", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:

                if (isNewNote){
                    finish();
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(MYNOTE,note);
                    setResult(DELETECODE,intent);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveNote(){
        mTitle = title.getText().toString().trim();
        mContent = content.getText().toString().trim();
        mTimeStamp = timestamp.getText().toString();
        mCharacters = String.valueOf(MyUtil.Counter(content.getText().toString()));

        if (!mContent.isEmpty() && !mTitle.isEmpty()){
            if (isNewNote){
            note = new Notes(mTitle,mContent,mCharacters,mTimeStamp);
            Intent intent = new Intent();
            intent.putExtra(MYNOTE,note);
            setResult(RESULT_OK,intent);
            }
            else if(!isNewNote){
               if (MyUtil.HasChanged(note,mTitle,mContent)){
                   Intent intent = new Intent();
                   note.setTitle(mTitle);
                   note.setContent(mContent);
                   note.setTimestamp(MyUtil.TimeStamp());
                   note.setCharachters(mCharacters);
                   intent.putExtra(MYNOTE,note);
                   setResult(RESULT_OK,intent);
                   Toast.makeText(this, "press back again", Toast.LENGTH_SHORT).show();
               }
               else {
                   Toast.makeText(this, "No change", Toast.LENGTH_SHORT).show();
               }
            }
        }
        else {
            Toast.makeText(this, "fields are empty \n press back again", Toast.LENGTH_SHORT).show();
        }

    }

    private void setProp(){
        if (isEditModeEnabled){
            title.setHint("set new title here");
            timestamp.setText(MyUtil.TimeStamp());
        }else{
            if (intent.hasExtra(MYNOTE)){
                note = intent.getParcelableExtra(MYNOTE);
                title.setText(note.getTitle());
                timestamp.setText(note.getTimestamp());
                content.setText(note.getContent());
                charachters.setText("characters "+note.getCharachters());
            }
        }
    }

    public void showKeyBoard(View view){
        if (view.requestFocus()){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public void shareIntent(){
        String mimeType = "text/plain";
        String text = title.getText().toString()+"\n"+content.getText().toString()
                +"\nlast modified "+timestamp.getText().toString();
        ShareCompat.IntentBuilder.from(this)
                .setType(mimeType)
                .setChooserTitle("send "+title.getText().toString())
                .setText(text)
                .startChooser();
    }

    @Override
    public void onBackPressed() {
        if (isEditModeEnabled){
            disableEditable();
            saveNote();
            //Toast.makeText(this, "back pressed", Toast.LENGTH_SHORT).show();
        }else
            super.onBackPressed();

    }
}