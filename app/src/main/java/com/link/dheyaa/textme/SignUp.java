package com.link.dheyaa.textme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public ProgressBar loading;
    View parentLayout;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());


        setContentView(R.layout.activity_sign_up);
        parentLayout = findViewById(R.id.container_main);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button) findViewById(R.id.btn_signUp);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        setTitle("sign up ");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    protected void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }

    }

    public void SignUp() {
        loading.setVisibility(View.VISIBLE);

        final EditText username = (EditText) findViewById(R.id.username);
        final EditText email = (EditText) findViewById(R.id.input_email);
        final EditText password = (EditText) findViewById(R.id.input_password);

        if (email.getText().length() != 0 && password.getText().length() != 0 && username.getText().length() != 0) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loading.setVisibility(View.INVISIBLE);
                                FirebaseUser authUser = mAuth.getCurrentUser();

                                User userObject = new User(
                                        username.getText().toString(),
                                        email.getText().toString(),
                                        new  HashMap<String , Boolean>()
                                );

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Users");
                                myRef.child(authUser.getUid()).setValue(userObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loading.setVisibility(View.INVISIBLE);

                                            FirebaseUser authUser = mAuth.getCurrentUser();
                                            updateUI(authUser);
                                        } else {
                                            Snackbar.make(parentLayout, "saving  failed", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                loading.setVisibility(View.INVISIBLE);
                                String errorMsg = task.getException().getMessage();
                                Snackbar.make( parentLayout , errorMsg, Snackbar.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }
                    });
        } else {
            loading.setVisibility(View.INVISIBLE);
            Snackbar.make(parentLayout, "Fill all of the fields ", Snackbar.LENGTH_LONG).show();
        }
    }

    public void SignInPage(View v) {

       // startActivity(new Intent(getApplicationContext(), SignIn.class));
        finish();
    }

}
