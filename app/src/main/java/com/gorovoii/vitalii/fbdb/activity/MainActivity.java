package com.gorovoii.vitalii.fbdb.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.gorovoii.vitalii.fbdb.R;
import com.gorovoii.vitalii.fbdb.adapter.NoteAdapter;
import com.gorovoii.vitalii.fbdb.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LoginActivity l = new LoginActivity();

    public static final String NOTES = "Notes";

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ArrayList<Note> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        updateUI();
        displayNotes();
    }

    private void updateUI() {

        mDataList = new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        mNoteAdapter = new NoteAdapter(this, mDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mNoteAdapter);
        mNoteAdapter.notifyDataSetChanged();
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

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDataList.clear();
                DataSnapshot tableNote = dataSnapshot.child(NOTES);

                for (DataSnapshot child: tableNote.getChildren()) {
                    Note tmpNote = child.getValue(Note.class);
                    mDataList.add(tmpNote);
                    mNoteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
