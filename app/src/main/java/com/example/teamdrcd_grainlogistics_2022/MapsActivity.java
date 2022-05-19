package com.example.teamdrcd_grainlogistics_2022;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

        CancellationToken ct = new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getCurrentLocation(100, ct)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            DatabaseReference myRef3 = database.getReference("/users/" + mAuth.getUid() + "/Location");
                            myRef3.setValue(location);
                        }
                    }
                });

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            DatabaseReference myRef3 = database.getReference("/users/" + mAuth.getUid() + "/Location");
                            myRef3.setValue(location);
                        }
                    }
                });

        DatabaseReference temp = database.getReference("/users/" + mAuth.getUid() + "/Farm ID");
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int fID = (int) dataSnapshot.getValue(Integer.class);
                DatabaseReference temp = database.getReference("/Farms/" + fID + "/people");
                temp.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> people = (ArrayList<String>) dataSnapshot.getValue();
                        for (int i = 0; i < people.size(); i++)
                        {
                            DatabaseReference temp = database.getReference("/users/" + people.get(i) + "/Location");
                            temp.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue() !=null) {
                                        Location loc = (Location)dataSnapshot.getValue();
                                        LatLng quadcities = new LatLng(loc.getLatitude(), loc.getLongitude());
                                        mMap.addMarker(new MarkerOptions().position(quadcities).title(""));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(quadcities));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Database below VV
        //tractor name
        DatabaseReference myRef = database.getReference("/Tractors" + "/Tractor1" + "/Tractor Name");
        myRef.setValue("Tractor1");

        //Fuel level
        DatabaseReference myRef1 = database.getReference("/Tractors" + "/Tractor1" + "/Tractor Fuel Level");
        myRef1.setValue("78%");

        //Grain capacity
        DatabaseReference myRef2 = database.getReference("/Tractors" + "/Tractor1" + "/Tractor Grain Capacity");
        myRef2.setValue("26% Full");

        //Grain moisture level
        DatabaseReference myRef3 = database.getReference("/Tractors" + "/Tractor1" + "/ Grain Moisture Level");
        myRef3.setValue("12% Moisture");
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

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
}