package com.example.marketplace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    //The id reference to the current user
    public static String user_id;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        FirebaseApp.initializeApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        final Intent main = new Intent(this, MainActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        findViewById(R.id.sign_up_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                EditText email = (EditText) findViewById(R.id.sign_up_email);
                EditText password = (EditText) findViewById(R.id.sign_up_password);
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                Intent activity = new Intent(SignUp.this, MainActivity.class);
                createAccount(emailText, passwordText, activity);
            }
        });

    }


    public void createAccount(String email, String password, final Intent activity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // If sign in fails, display a message to the user.
                            Log.w("Fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        } else {
                            Log.w("Success", "createUserWithEmail:success");

                            // Sign in success, update UI with the signed-in user's information
                            EditText nameText = (EditText) findViewById(R.id.sign_up_name);
                            String name = nameText.getText().toString();

                            EditText emailText = (EditText) findViewById(R.id.sign_up_email);
                            String email = emailText.getText().toString();

                            EditText phoneText = (EditText) findViewById(R.id.sign_up_phone);
                            String phone = phoneText.getText().toString();

                            FirebaseUser user = mAuth.getCurrentUser();
                            user_id = user.getUid();
                            User.createUser(user_id, name, email, phone);
                            startActivity(activity);
                            finish();

                            /*updateUI(user);*/
                        }

                        // ...
                    }
                });
    }
}
