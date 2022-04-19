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
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mMessageEditText;
    private EditText pMessageEditText;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
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
         //Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

    }


    private void updateUI(FirebaseUser currentUser) {
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
                            database.getReference("/Farms/one").setValue(new Farm());
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
                                    userError = error.substring(i + 2);
                                }
                            }
                            if (userError.contains("password") || userError.contains("identifier"))
                            {
                                userError = "Your email or password is incorrect.";
                            }
                            userError.replace("The user account", "Your account");
                            userError.replace("The user's", "Your");
                            userError.replace("The user", "You");
                            String suc = "Login Failed: " + userError;
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