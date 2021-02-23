package com.fizha.appnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseHelper db;
    private ArrayList<NoteModel> allNote = new ArrayList<>();
    private NoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieAnimationView empty = findViewById(R.id.empty);
        TextView txt1 = findViewById(R.id.txt1);
        RecyclerView listNote = findViewById(R.id.list_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listNote.setLayoutManager(linearLayoutManager);
        listNote.setItemAnimator(new DefaultItemAnimator());
        db = new DatabaseHelper(this);
        allNote = db.selectNoteData();

        if (allNote.size() > 0){
            listNote.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            txt1.setVisibility(View.GONE);
            mAdapter = new NoteAdapter(this, allNote);
            listNote.setAdapter(mAdapter);
        } else {
            listNote.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
        }

        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton)findViewById(R.id.add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null){
            db.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter!=null)
                    mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void addNoteDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_note_layout, null);

        final TextInputEditText txtTitle = (TextInputEditText)subView.findViewById(R.id.et_title);
        final TextInputEditText txtDesc = (TextInputEditText)subView.findViewById(R.id.et_desc);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new note");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String eTitle = txtTitle.getText().toString();
                final String eDesc = txtDesc.getText().toString();
                final String tgl = getDateTime();

                if(TextUtils.isEmpty(eTitle)){
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else{
                    NoteModel note = new NoteModel(eTitle, eDesc, tgl);
                    db.insert(note);
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private String getDateTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "MMMM dd, yyyy", Locale.getDefault()
        );
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
}