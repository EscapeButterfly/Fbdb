package com.gorovoii.vitalii.fbdb.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import com.gorovoii.vitalii.fbdb.service.DevToDevIntent;

import static com.gorovoii.vitalii.fbdb.activity.MainActivity.NOTES;

public class AddNoteActivity extends AppCompatActivity {

    public static final String REQUIRED = "Required";
    private static final String TOKEN =
            "cL0zizW4Opk:APA91bGguwz24W0wutvJc1ZAKdY2hQhNCD0Yk3td_GOnQ60Q3SFapWKNsacFR8vZ7w6PsowYhc2Fsmx1-DeLw3meRpnLPqh3niGE1ltcef27mdgLA4XyjU0gTN0QDpFcCnefSOpIYSEQ";

    EditText addTtl;
    EditText addBody;
    Button saveBtn;
    Button sendNtfct;

    private BroadcastReceiver mMessageReceiver;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getExtras().getString("title");
                String body = intent.getExtras().getString("body");
                Toast.makeText(AddNoteActivity.this, title+"\n"+body, Toast.LENGTH_LONG).show();
            }
        };

        sendNtfct = (Button)findViewById(R.id.btnSendNtfct);
        if (sendNtfct == null) {
            Log.d("Layout trouble", "Check your layout");
        }
        sendNtfct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SYNC, null, AddNoteActivity.this, DevToDevIntent.class);
                startService(intent);
    //              Intent intentForService = new Intent(AddNoteActivity.this, DevToDevIntent.class);
   //             intentForService.putExtra("title",TOKEN);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        initUI();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    };

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    };

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
