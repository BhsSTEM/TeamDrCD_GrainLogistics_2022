package com.example.teamdrcd_grainlogistics_2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText mMessageEditText;
    private EditText pMessageEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();



    }

    public void NewUser(View view) {
        Intent intent = new Intent(this, NewUser.class);
        startActivity(intent);
    }

    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //updateUI(user);
        }
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null)
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    public void login(View view) {
        mMessageEditText = findViewById(R.id.editTextTextPersonName);
        String message = mMessageEditText.getText().toString();
        pMessageEditText = findViewById(R.id.editTextTextPassword2);
        String password = pMessageEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(message, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String suc = "";
                            final TextView helloTextView = (TextView) findViewById(R.id.textView);
                            helloTextView.setText(suc);
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            String error = task.getException().toString();
                            String userError = "";
                            for (int i = 0; i < error.length(); i++)
                            {
                                if (error.charAt(i) == ':')
                                {
                                    userError = error.substring(i);
                                }
                            }
                            String suc = "Login Failed: ";
                            final TextView helloTextView = (TextView) findViewById(R.id.textView);
                            helloTextView.setText(suc);
                        }
                    }
                }
                );
    }

    public void forgot(View view) {
        Intent intent = new Intent(this, forgot_password.class);
        startActivity(intent);
    }

}