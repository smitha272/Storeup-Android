package com.storeup;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AdminAnalytics extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mview;
    private double lat;
    private double lng;
    private JSONObject js;
    private ArrayList<ArrayList<String>> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_analytics);
        Bundle bundle = getIntent().getExtras();
        try {
            js = new JSONObject(bundle.getString("jsonObj"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (js.length() == 0 || js == null) {
            Toast.makeText(getApplicationContext(), "There are no suggestions for this location", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Iterator iter = js.keys();
                String key = null;
                while (iter.hasNext()) {
                    key = (String) iter.next();
                    ArrayList<String> list = new ArrayList<>();
                    list.add(key);
                    list.add(js.getString(key));
                    arr.add(list);
                }
            }catch(JSONException e){
                    e.printStackTrace();
                }
            }

        //tv.setText("Location Suggested\nLatitude: " + message+"\nLongitude: "+msg);
        mMapView = (MapView)findViewById(R.id.map1);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        Geocoder geocoder;
        List<Address> addresses;
        mGoogleMap = googleMap;
        try {
            for (ArrayList<String> lst : arr) {
                geocoder = new Geocoder(this, Locale.getDefault());
                lat = Double.parseDouble(lst.get(0));
                lng = Double.parseDouble(lst.get(1));
                addresses = geocoder.getFromLocation(lat, lng, 1);
                String address = addresses.get(0).getAddressLine(0);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(address));
                CameraPosition abc = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(16).bearing(0).tilt(45).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(abc));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
