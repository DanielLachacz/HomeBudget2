package com.example.daniellachacz.homebudget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mLoginBtn;
    private Button mRegisterBtn;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.passwordField);

        mLoginBtn = findViewById(R.id.loginBtn);
        mRegisterBtn = findViewById(R.id.registerBtn);

        mProgress = new ProgressDialog(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }


            }
        };


        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                startSignIn();


            }
        });
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(int1);
            }
        });
    }


    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

            Toast.makeText(LoginActivity.this, "Pola są puste", Toast.LENGTH_LONG).show();

        } else {

            mProgress.setMessage("Logowanie...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Problem z logowaniem", Toast.LENGTH_LONG).show();

                    } else {

                        checkUserExist();
                    }
                }
            });
        }
    }

    private void checkUserExist(){

        final String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id)){

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
