package com.example.marketplace;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Activity {
    private FirebaseAuth mAuth;



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //if currentUser is not null, then updateUI for that user
        //updateUI(currentUser);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Intent main = new Intent(this, MainActivity.class);
        final Intent sign_up = new Intent(this, SignUp.class);


        FirebaseApp.initializeApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById(R.id.login_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EditText email = (EditText) findViewById(R.id.login_email);
                EditText password = (EditText) findViewById(R.id.login_password);
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                signIn(emailText, passwordText, main);


            }
        });
        findViewById(R.id.login_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(sign_up);
                finish();
            }
        });
    }

    public void signIn(String email, String password, final Intent activity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("Success", user.getEmail());
                            startActivity(activity);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Log.d("fail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
