package com.example.myNotes.Ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myNotes.Adapter.MyAdapter;
import com.example.myNotes.Entity.Notes;
import com.example.myNotes.R;
import com.example.myNotes.ViewModels.NoteViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUESTCODE1 = 0;
    private static final int REQUESTCODE2 = 10;

    public static final String STRING_EXRTRA = "extraString";
    private NoteViewModel viewModel;

    //views
    FloatingActionButton mFab;
    RecyclerView mRecyclerView;
    MyAdapter mAdapter;
    Toolbar toolbar;

    //
    SharedPreferences sharedPreferences;
    public static int Mode;
    private ActionMode mActionMode;
    private boolean isMultiSelect;
    private ArrayList<Notes> selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SettingActivity.PREFERENCES_NIGHTMODE, MODE_PRIVATE);
        Mode = sharedPreferences.getInt(SettingActivity.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(Mode);
        setContentView(R.layout.activity_main);
        findingIds();
        setToolbar();
        setupRecyclerView();
        setListeners();
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        viewModel.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                Toast.makeText(MainActivity.this, "observerd", Toast.LENGTH_SHORT).show();
                mAdapter.submitList(notes);
            }
        });


    }

    private void findingIds() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mFab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");
    }

    private void setupRecyclerView() {

        mAdapter = new MyAdapter();
        mAdapter.setMainColor(getColor());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setSaveEnabled(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void setListeners() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(STRING_EXRTRA, getLocalClassName());
                startActivityForResult(intent, REQUESTCODE1);
            }
        });

        mAdapter.setonItemTouchListener(new MyAdapter.onItemTouchListener() {
            @Override
            public void onItemClick(Notes notes) {
                if (!isMultiSelect) {
                    Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                    intent.putExtra(STRING_EXRTRA, "MyAdapter");
                    intent.putExtra(NoteActivity.MYNOTE, notes);
                    startActivityForResult(intent, REQUESTCODE2);
                } else multiSelect(notes);
            }

            @Override
            public void onItemLongClick(Notes notes) {
                if (!isMultiSelect) {
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(mActionModeCallBack);
                        mFab.setVisibility(View.GONE);
                    }
                }
                multiSelect(notes);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUESTCODE1 == requestCode && resultCode == RESULT_OK) {
            Notes note = data.getParcelableExtra(NoteActivity.MYNOTE);
            viewModel.insert(note);
            Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
        } else if (REQUESTCODE2 == requestCode && resultCode == RESULT_OK) {
            Notes note = data.getParcelableExtra(NoteActivity.MYNOTE);
            viewModel.update(note);
            Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
        } else if (REQUESTCODE2 == requestCode && resultCode == NoteActivity.DELETECODE){
            Notes note = data.getParcelableExtra(NoteActivity.MYNOTE);
            viewModel.delete(note);
        }
    }


    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper
            .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            viewModel.delete(mAdapter.getNoteAt(viewHolder.getAdapterPosition()));
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                getAlertDialog("Do you really want to delete all the notes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {

                                    case DialogInterface.BUTTON_NEUTRAL:
                                        Toast.makeText(MainActivity.this, "cancelled " + which, Toast.LENGTH_SHORT).show();
                                        break;
                                    case DialogInterface.BUTTON_POSITIVE:
                                        viewModel.deleteAllNotes();
                                }

                            }

                        }).show();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getInt(SettingActivity.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) != AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt(SettingActivity.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        }
    }

    public ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
            mode.setTitle("choose your optn");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.select_none:
                    selectedList.clear();
                    mAdapter.setSelectedList(selectedList);
                    mode.finish();
                    return true;
                case R.id.select_all:

                    List<Notes> list = mAdapter.getCurrentList();
                    selectedList.clear();
                    for (Notes notes:list
                         ) {
                        
                        selectedList.add(notes);
                    }
                    mAdapter.setSelectedList(selectedList);
                    mode.setTitle("selected notes "+selectedList.size());
                    //mode.finish();
                    return true;
                case R.id.delete:
                    Toast.makeText(MainActivity.this, "deleting", Toast.LENGTH_SHORT).show();
                    if (selectedList.size() > 0)
                        for (Notes note : selectedList
                        ) {
                            viewModel.delete(note);
                        }
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            selectedList = null;
            mAdapter.setSelectedList(new ArrayList<Notes>());
            mFab.setVisibility(View.VISIBLE);
        }
    };


    public int getColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.cardbackground, typedValue, true);
        return typedValue.data;
    }

    private void multiSelect(Notes notes) {
        if (selectedList == null)
            selectedList = new ArrayList<>();
        if (mActionMode != null) {
            if (selectedList.contains(notes))
                selectedList.remove(notes);
            else selectedList.add(notes);

            if (selectedList.size() > 0) {
                mActionMode.setTitle("Selected Items " + selectedList.size());
                mAdapter.setSelectedList(selectedList);
            } else {
                mActionMode.finish();
                isMultiSelect = false;
                selectedList = null;
            }

        }
    }


    public MaterialAlertDialogBuilder getAlertDialog(String title, DialogInterface.OnClickListener listener) {

        return new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setNeutralButton("Cancel", listener)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "yes " + which, Toast.LENGTH_SHORT).show();
                        viewModel.deleteAllNotes();
                    }
                });
    }


}