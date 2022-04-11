package com.example.teamdrcd_grainlogistics_2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewUser extends AppCompatActivity {
    private EditText firstName;
    private EditText emailBox;
    private EditText passwordBox1;
    private EditText passwordBox2;
    private EditText lastName;
    private EditText phoneNum;

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
                            updateUI(user);
                            assert user != null;
                            String uid = user.getUid();

                            firstName = findViewById(R.id.editTextTextPersonName4);
                            String fName = firstName.getText().toString();
                            DatabaseReference myRef = database.getReference("/users/" + uid + "/First Name");
                            myRef.setValue(fName);

                            lastName = findViewById(R.id.editTextTextPersonName5);
                            String lName = lastName.getText().toString();
                            DatabaseReference myRef1 = database.getReference("/users/" + uid + "/Last Name");
                            myRef1.setValue(lName);

                            phoneNum = findViewById(R.id.editTextTextPersonName7);
                            Long pn = Long.parseLong(phoneNum.getText().toString());
                            DatabaseReference myRef2 = database.getReference("/users/" + uid + "/Phone Number");
                            myRef2.setValue(pn);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }
}