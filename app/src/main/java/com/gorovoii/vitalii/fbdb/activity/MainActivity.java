package com.gorovoii.vitalii.fbdb.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gorovoii.vitalii.fbdb.R;
import com.gorovoii.vitalii.fbdb.adapter.NoteAdapter;
import com.gorovoii.vitalii.fbdb.model.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "User info";

    public static final String NOTES = "Notes";

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ArrayList<Note> mDataList;

    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null) {
            mUser.getToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                Log.e("tokechRefresh", idToken);
                            }
                        }
                    });
        } else {
            Log.e("TAG", "user is invalid");
        }

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            String email = user.getEmail();
//
//            Log.d(TAG, "Email: " + email);
//        }

//        deleteBtn = (Button) findViewById(R.id.deleteBtn);
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteNote();
//            }
//        });

        updateUI();
        displayNotes();
    }

//    private void deleteNote() {
//        for(Note i : mDataList) {
//            mDataList.remove(i);
//        }
//    }

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
            LoginActivity.signout();
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

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getExtras().getString("title");
            String body = intent.getExtras().getString("body");
            Toast.makeText(MainActivity.this, body+"\n"+title, Toast.LENGTH_LONG).show();
        }
    };

}
