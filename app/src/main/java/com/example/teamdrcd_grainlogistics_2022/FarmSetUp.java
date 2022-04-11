package com.example.teamdrcd_grainlogistics_2022;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.teamdrcd_grainlogistics_2022.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class FarmSetUp extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    //private int[] colors = new int[0xff388E3C];
    private ArrayList<Polygon> polyList= new ArrayList<Polygon>();
    String[] locs = new String[4];
    Polygon polygon1;

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

        // Add a marker in Bettendorf and move the camera
        LatLng quadcities = new LatLng(42, -90);
        mMap.addMarker(new MarkerOptions().position(quadcities).title("Marker in Bettendorf"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(quadcities));

        //on touch stores coordinates to send to make a polygon
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Log.d("DEBUG", point.toString());
                if(locs[0] == null){
                    locs[0] = point.toString();
                } else if(locs[1] == null){
                    locs[1] = point.toString();
                } else if(locs[2] == null){
                    locs[2] = point.toString();
                } else if(locs[3] == null){
                    locs[3] = point.toString();
                    markArea(locs);
                }
            }
        });
    }
    //takes in a list of coordinate strings and uses them to make a polygon
    public void markArea(String[] locs){
        double lat1 = 0;
        double lng1 = 0;
        double lat2 = 0;
        double lng2 = 0;
        double lat3 = 0;
        double lng3 = 0;
        double lat4 = 0;
        double lng4 = 0;
        for(int i = 0;i<locs.length;i++){
            String x = locs[i];
            double lat = Double.parseDouble(x.substring(x.indexOf("(") + 1, x.indexOf("(") + 8));
            double lng = Double.parseDouble(x.substring(x.indexOf(",")+1, x.indexOf(",") + 8));
            if(i == 0) {
                lat1 = lat;
                lng1 = lng;
            } else if(i == 1) {
                lat2 = lat;
                lng2 = lng;
            } else if(i == 2) {
                lat3 = lat;
                lng3 = lng;
            } else if(i == 3) {
                lat4 = lat;
                lng4 = lng;
            }
        }
        polygon1 = mMap.addPolygon(new PolygonOptions().clickable(true)
                .add(
                        new LatLng(lat1, lng1),
                        new LatLng(lat2, lng2),
                        new LatLng(lat3, lng3),
                        new LatLng(lat4, lng4)));
        polygon1.setStrokeColor(0xff388E3C);
    }
    //adds the polygon to a permanently stored list
    public void addPoly(){
        Polygon storePoly = polygon1;
        polyList.add(storePoly);
        polygon1.remove();
        locs[0] = null;
        locs[1] = null;
        locs[2] = null;
        locs[3] = null;
    }
    //gets rid of the current polygon
    public void resetPoly(){
        polygon1.remove();
        locs[0] = null;
        locs[1] = null;
        locs[2] = null;
        locs[3] = null;
    }
}
