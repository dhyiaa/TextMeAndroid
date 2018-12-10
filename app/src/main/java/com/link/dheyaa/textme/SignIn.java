package com.link.dheyaa.textme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressBar loading ;
    View parentLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());

        setContentView(R.layout.activity_sign_in);

        parentLayout = findViewById(R.id.container_main);

        Button button = (Button) findViewById(R.id.btn_signIn);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        setTitle("sign in ");

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SignIn();
            }
        });

    }

    protected void updateUI(FirebaseUser currentUser ){
        if(currentUser != null ){
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }

    }

    public void SignIn(){

        loading.setVisibility(View.VISIBLE);

        EditText email = (EditText) findViewById(R.id.input_email);
        EditText password = (EditText) findViewById(R.id.input_password);

        if(email.getText().length()  != 0 && password.getText().length()  != 0 ){
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                loading.setVisibility(View.INVISIBLE);

                                Snackbar.make( parentLayout , "Authentication failed", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();


                                updateUI(null);
                            }
                        }
                    });
        }else{

            Snackbar.make( parentLayout , "fill all of the fields !", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            loading.setVisibility(View.INVISIBLE);

        }
    }

    public void SignUpPage(View v){
        startActivity(new Intent(getApplicationContext(), SignUp.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
