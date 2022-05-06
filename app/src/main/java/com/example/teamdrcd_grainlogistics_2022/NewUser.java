package com.example.teamdrcd_grainlogistics_2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;


public class NewUser extends AppCompatActivity {
    private EditText firstName;
    private EditText emailBox;
    private EditText passwordBox1;
    private EditText passwordBox2;
    private EditText lastName;
    private EditText phoneNum;
    private EditText farmID;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        //mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void submit(View view) {



        emailBox = findViewById(R.id.editTextTextPersonName2);
        String email = emailBox.getText().toString();

        passwordBox1 = findViewById(R.id.editTextTextPersonName6);
        String password1 = passwordBox1.getText().toString();

        passwordBox2 = findViewById(R.id.editTextTextPersonName8);
        String password2 = passwordBox1.getText().toString();

        String password = "";
        if(password1.equals(password2))
        {
            password = password1;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            String uid = user.getUid();

                            firstName = findViewById(R.id.editTextTextPersonName4);
                            String fName = firstName.getText().toString();
                            fName = fName.toLowerCase();
                            String finitial = fName.substring(0,1);
                            fName = finitial.toUpperCase() + fName.substring(1);
                            DatabaseReference myRef = database.getReference("/users/" + uid + "/First Name");
                            myRef.setValue(fName);

                            lastName = findViewById(R.id.editTextTextPersonName5);
                            String lName = lastName.getText().toString();
                            lName = lName.toLowerCase();
                            String linitial = lName.substring(0,1);
                            lName = linitial.toUpperCase() + lName.substring(1);
                            DatabaseReference myRef1 = database.getReference("/users/" + uid + "/Last Name");
                            myRef1.setValue(lName);

                            phoneNum = findViewById(R.id.editTextTextPersonName7);
                            Long pn = 0L;

                            try {
                                pn = Long.parseLong(phoneNum.getText().toString());
                            }
                            catch (Exception e)
                            {
                                String sPhoneNum = phoneNum.getText().toString();
                                sPhoneNum.replace("-", "");
                                sPhoneNum.replace("(", "");
                                sPhoneNum.replace(")", "");
                                sPhoneNum.replace(" ", "");
                                pn = Long.parseLong((sPhoneNum));
                            }

                            DatabaseReference myRef2 = database.getReference("/users/" + uid + "/Phone Number");
                            myRef2.setValue(pn);

                            farmID = findViewById(R.id.editTextTextPersonName3);
                            int fID = Integer.parseInt(farmID.getText().toString());
                            Farm farm = new Farm(fID);
                            farm.addUser(uid);

                            DatabaseReference myRef3 = database.getReference("/users/" + uid + "/numOfFields");
                            myRef3.setValue("0");

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            //Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            String exception = task.getException().toString();
                            for(int i = 0; i < exception.length(); i++)
                            {
                                if(exception.charAt(i) == ':')
                                {
                                    exception = exception.substring(i + 2);
                                }
                            }

                            String suc = "Login Failed: \n" + exception;
                            final TextView helloTextView = (TextView) findViewById(R.id.textView10);
                            helloTextView.setText(suc);

                        }
                    }
                });

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    String suc = "Login Failed. Your email or password was incorrect.";
                                    //final TextView helloTextView = (TextView) findViewById(R.id.textView);
                                    //helloTextView.setText(suc);
                                }
                            }
                        }
                );
    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(NewUser.this, FarmSetUp.class));
    }
}