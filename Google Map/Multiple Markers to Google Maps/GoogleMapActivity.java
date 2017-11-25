package karun.com.googlemapdemo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // compile 'com.google.android.gms:play-services:9.2.0'


    /* <meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSyDdrtaTSjzTliPN3b8ZlywzMO_IJkr54Z8"/>


      <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
*/





    private GoogleMap googleMap;
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;

    ArrayList<HashMap<String, String>> location = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);


        // *** Display Google Map

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;


        // Location 1
        map = new HashMap<String, String>();
        map.put("LocationID", "1");
        map.put("Latitude", "25.043652");
        map.put("Longitude", "83.798440");
        map.put("LocationName", "Kudra");
        location.add(map);

        // Location 2
        map = new HashMap<String, String>();
        map.put("LocationID", "2");
        map.put("Latitude", "25.048276");
        map.put("Longitude", "83.787970");
        map.put("LocationName", "Kudra parkhand");
        location.add(map);

        // Location 3
        map = new HashMap<String, String>();
        map.put("LocationID", "3");
        map.put("Latitude", "25.048422");
        map.put("Longitude", "83.790867");
        map.put("LocationName", "Kudra SBI");
        location.add(map);

        // Location 4
        map = new HashMap<String, String>();
        map.put("LocationID", "4");
        map.put("Latitude", "25.051209");
        map.put("Longitude", "83.787855");
        map.put("LocationName", "Kudra Rain Basera");
        location.add(map);


        // *** Focus & Zoom
        Latitude = Double.parseDouble(location.get(0).get("Latitude").toString());
        Longitude = Double.parseDouble(location.get(0).get("Longitude").toString());
        LatLng coordinate = new LatLng(Latitude, Longitude);
        googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 14));

        // *** Marker (Loop)
        for (int i = 0; i < location.size(); i++) {
            Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
            Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
            String name = location.get(i).get("LocationName").toString();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
            googleMap.addMarker(marker);

        }
    }
}