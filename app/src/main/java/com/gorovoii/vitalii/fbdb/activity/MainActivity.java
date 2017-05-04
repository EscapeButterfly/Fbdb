package com.gorovoii.vitalii.fbdb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.gorovoii.vitalii.fbdb.R;
import com.gorovoii.vitalii.fbdb.adapter.NoteAdapter;
import com.gorovoii.vitalii.fbdb.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LoginActivity l = new LoginActivity();

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;



    List<Note> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data.add(new Note("Zametka", "body"));
        data.add(new Note("Zametka1", "body"));
        data.add(new Note("Zametka2", "body"));
        displayNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.actionAdd) {
            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.actionSignOut){
            l.signout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayNotes() {

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        mNoteAdapter = new NoteAdapter(this,data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mNoteAdapter);
        mNoteAdapter.notifyDataSetChanged();

    }

}
