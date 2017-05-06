package com.gorovoii.vitalii.fbdb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gorovoii.vitalii.fbdb.R;
import com.gorovoii.vitalii.fbdb.model.Note;

import static com.gorovoii.vitalii.fbdb.activity.MainActivity.NOTES;

public class AddNoteActivity extends AppCompatActivity {

    public static final String REQUIRED = "Required";

    EditText addTtl;
    EditText addBody;
    Button saveBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mAuth = FirebaseAuth.getInstance();

        initUI();

    }

    private void initUI() {
        addTtl = (EditText)findViewById(R.id.addTitle);
        addBody = (EditText)findViewById(R.id.addBody);
        saveBtn = (Button)findViewById(R.id.btnSave);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(addTtl.getText())) {
                    addTtl.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(addBody.getText())) {
                    addBody.setError(REQUIRED);
                    return;
                }

                View view = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view != null ? view.getWindowToken() : null, 0);

                setEditingEnabled(false);
                postNote(new Note(addTtl.getText().toString(), addBody.getText().toString()));
                Toast.makeText(AddNoteActivity.this,"Posting...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postNote(Note note) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();

        String key = mDbRef.child(NOTES).push().getKey();

        mDbRef.child(NOTES).child(key).setValue(note, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                saveBtn.setEnabled(true);

                if (databaseError == null) {
                    Toast.makeText(AddNoteActivity.this, "Successfully posted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(AddNoteActivity.this, "Note was not posted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setEditingEnabled(boolean state) {
        addTtl.setEnabled(state);
        addBody.setEnabled(state);
    }

}
