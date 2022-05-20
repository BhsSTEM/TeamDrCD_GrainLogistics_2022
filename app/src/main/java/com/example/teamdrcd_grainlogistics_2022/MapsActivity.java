package com.example.teamdrcd_grainlogistics_2022;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.teamdrcd_grainlogistics_2022.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private int numOfTractors = 1;
    private boolean placeTractor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Add a marker in Sydney and move the camera
        LatLng quadcities = new LatLng(42, -90);
        mMap.addMarker(new MarkerOptions().position(quadcities).title("Marker in Bettendorf"));
        float zoomLevel = (float) 16.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quadcities, zoomLevel));
        // Add a marker in Sydney and move the camera
        LatLng Tractor1 = new LatLng(41.557579, -90.495911);
        MarkerOptions markerOptions = new MarkerOptions().position(Tractor1).title("Tractor #1")
                // below line is use to add custom marker on our map.
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_agriculture_24));
        //Marker marker = new Marker(markerOptions);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tractor1));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if(placeTractor){
                    addTractor(latLng);
                }
            }
        });
    }
    public void switchActivities(View view) {
        Intent switchActivityIntent = new Intent(this, FarmSetUp.class);
        startActivity(switchActivityIntent);
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(20, 20, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void addTractor(LatLng latlng){
        String temp = "Tractor #" +  String.valueOf(numOfTractors + 1);
        MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(temp)
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_agriculture_24));
        mMap.addMarker(markerOptions);
        numOfTractors += 1;
        placeTractor = false;
    }
    public void goTrue(View view) { placeTractor = true;}
}