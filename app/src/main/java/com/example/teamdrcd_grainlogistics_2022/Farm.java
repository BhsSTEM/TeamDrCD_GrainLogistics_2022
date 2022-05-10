package com.example.teamdrcd_grainlogistics_2022;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class Farm {
    public int ID;
    public ArrayList<String> people = new ArrayList<String>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();



    public Farm(int id)
    {
        ID = id;
    }

    public void addUser(String uid)
    {

        people = getUsers();
        people.add(uid);
        DatabaseReference myRef1 = database.getReference("/Farms/" + ID +"/people");
        myRef1.setValue(people);

    }

    public ArrayList<String> getUsers()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference temp = database.getReference("/Farms/" + ID +"/people");
        final boolean[] flag = {false};

        final ArrayList<String>[] result = new ArrayList[]{new ArrayList<String>()};
        //while (!flag[0]) {
            temp.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        //Log.e("firebase", "Error getting data", task.getException());

                    } else {
                        flag[0] = true;
                        result[0] = (ArrayList<String>) task.getResult().getValue();
                        //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    }
                }
            });
        //}
        while(result[0] == null) {
            if (result[0] != null)
                return result[0];
        }
        return result[0];
    }
}
