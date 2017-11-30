package com.storeup;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.storeup.Helpers.ImageLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class Receipt_details extends Fragment implements OnMapReadyCallback {

    private ImageView imageView2;
    private TextView storeName;
    private TextView storeAddress;
    private String latitude="0";
    private String longitude = "0";
    private double lat = 0.0;
    private double lng = 0.0;
    private View mview;
    private boolean flag =false;
    private String store_name;
    private String store_address;
    private GoogleMap mGoogleMap;
    private MapView mMapView;



    public Receipt_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview=  inflater.inflate(R.layout.fragment_receipt_details, container, false);
        Bundle bundle = getArguments();
        store_name = bundle.getString("store_name");
        store_address = bundle.getString("store_address");
        String download_url = bundle.getString("download_url");
        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");
        System.out.println("latitude "+latitude+" longitude "+longitude);

        if((latitude=="0" && longitude=="0") || (latitude==null && longitude==null)){
            flag=false;
        }else{
            System.out.println("latitude "+latitude);
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
        }

        imageView2 = (ImageView)mview.findViewById(R.id.imageView2);
        storeName = (TextView)mview.findViewById(R.id.storeName);
        storeAddress = (TextView)mview.findViewById(R.id.storeAddress);

        storeName.setText(store_name);
        storeAddress.setText(store_address);

        new ImageLoader(download_url, imageView2).execute();

        return mview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        mMapView = (MapView)mview.findViewById(R.id.map);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(store_name).snippet(store_address));
        CameraPosition abc = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(abc));
    }
}
