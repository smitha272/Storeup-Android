package com.storeup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.storeup.Adapters.AdminAnalyticsAdapter;
import com.storeup.Entity.AdminAnalyticsDetails;

import java.util.ArrayList;

public class AdminAnalytics extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mview;
    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_analytics);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("Latitude");
        String msg = bundle.getString("Long");

        lat = Double.parseDouble(message);
        lng = Double.parseDouble(msg);

        System.out.println("Checkmsg0 " + msg);
        //TextView tv = (TextView) findViewById(R.id.lat);

        //tv.setText("Location Suggested\nLatitude: " + message+"\nLongitude: "+msg);
        mMapView = (MapView)findViewById(R.id.map1);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        //tv2.setText("Longitude: " + msg);
//        ArrayList<AdminAnalyticsDetails> myList = (ArrayList<AdminAnalyticsDetails>) getIntent().getSerializableExtra("suggestionList");
       /* AdminAnalyticsAdapter adminAdapter = new AdminAnalyticsAdapter(this, myList);
        ListView listView = (ListView) findViewById(R.id.listview_fl);
        listView.setAdapter(adminAdapter);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        mGoogleMap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Suggested Location"));
        CameraPosition abc = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(abc));
    }
}
