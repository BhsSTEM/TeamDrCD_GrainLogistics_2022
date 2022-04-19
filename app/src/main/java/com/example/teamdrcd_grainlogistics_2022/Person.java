package com.example.teamdrcd_grainlogistics_2022;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class Person {
    public FirebaseAuth mAuth;
    public String uid;
    public String firstName;
    public String lastName;
    public Long phoneNumber;
    public boolean admin;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //gets
    public String getUid()
    {
        return mAuth.getUid();
    }

    public String getFirstName()
    {
        database.getReference("/users/" + uid + "/First Name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    firstName = String.valueOf(task.getResult().getValue());
                }
            }
        });
        return firstName;
    }

    public String getLastName()
    {
        database.getReference("/users/" + uid + "/Last Name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    lastName = String.valueOf(task.getResult().getValue());
                }
            }
        });
        return lastName;
    }

    public Long getPhoneNumber()
    {
        database.getReference("/users/" + uid + "/Phone Number").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    phoneNumber = Long.valueOf(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        return phoneNumber;
    }

    public boolean isAdmin()
    {
        return admin;
    }
}
