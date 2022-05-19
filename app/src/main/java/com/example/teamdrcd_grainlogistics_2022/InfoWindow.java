package com.example.teamdrcd_grainlogistics_2022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class InfoWindow extends AppCompatActivity {

    ImageButton myImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        myImageButton = (ImageButton) findViewById(R.id.infobutton);

        myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadNewActivity = new Intent(InfoWindow.this, InfoForMaps.class);
                startActivity(intentLoadNewActivity);
            }
        });
    }
}
