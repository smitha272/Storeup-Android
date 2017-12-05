package com.storeup.Adapters;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.storeup.AdminAnalytics;
import com.storeup.Entity.AdminAnalyticsDetails;
import com.storeup.Entity.UserReceiptDetails;
import com.storeup.Helpers.ImageLoader;
import com.storeup.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminAnalyticsAdapter extends ArrayAdapter<AdminAnalyticsDetails> {

    private static final String LOG_TAG = AdminAnalyticsAdapter.class.getSimpleName();
    public AdminAnalyticsAdapter(Activity context, ArrayList<AdminAnalyticsDetails> adminAnalytics) {
        super(context, 0, adminAnalytics);
    }
    boolean val = true;

    @Override
    public boolean isEnabled (int position) {

        return val;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.admin_analytics_view, parent, false);
        }

        AdminAnalyticsDetails adminAnalyticsDetails = getItem(position);
        TextView storeNameView = (TextView) listItemView.findViewById(R.id.store_name);
        TextView storeAddressView = (TextView) listItemView.findViewById(R.id.store_address);
       /* AdminAnalytics adminAnalytics = new AdminAnalytics();*/



      /*  Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(adminAnalytics.getBaseContext(), Locale.getDefault());

        Double lat = Double.valueOf(adminAnalyticsDetails.getLatitude());
        Double lng = Double.valueOf(adminAnalyticsDetails.getLongitude());
        String address="";

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        storeNameView.setText(adminAnalyticsDetails.getLatitude());
        storeAddressView.setText(adminAnalyticsDetails.getLongitude());

        return listItemView;
    }

}
