package com.gorovoii.vitalii.fbdb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gorovoii.vitalii.fbdb.R;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText email;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //User is signed in

                } else {
                    //User is signed out

                }

            }
        };

        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_registration).setOnClickListener(this);
        findViewById(R.id.btn_sign_in).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_sign_in){
            signin(email.getText().toString(), password.getText().toString());
        } else if (v.getId() == R.id.btn_registration) {
            registration(email.getText().toString(), password.getText().toString());
        }

    }
    public void signin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT);
                            // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            // startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Авторизация провалена", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
    public void registration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(LoginActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
}