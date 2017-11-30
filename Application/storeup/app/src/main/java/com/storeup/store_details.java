package com.storeup;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class store_details extends Fragment {

    private String storeName;
    private String email;
    private ImageView imgView;
    private TextView s;
    private TextView distance;

    private String url = "http://10.0.2.2:3000/store/storeDetails";

    public store_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_store_details, container, false);
        Bundle bundle = getArguments();
        storeName = bundle.getString("store_name");
        email = bundle.getString("user_email");
        imgView = (ImageView)view.findViewById(R.id.storeImage);
        s = (TextView)view.findViewById(R.id.storeName);
        distance = (TextView)view.findViewById(R.id.distance);
        System.out.println("Store Name: "+storeName);
        if(storeName.equals("Costco")){
            imgView.setImageResource(R.drawable.costco);
        }else if(storeName.equals("Walmart")){
            imgView.setImageResource(R.drawable.walmart);
        }else if(storeName.equals("Target")){
            imgView.setImageResource(R.drawable.target);
        }else{
            imgView.setImageResource(R.drawable.store);
        }


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getStoreDetails();
    }

    public void getStoreDetails(){

/*        System.out.println("Store name is:"+storeName);
        System.out.println("User name is:"+email);*/
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray userDetails = new JSONArray();
                            userDetails = response.getJSONArray("userDetails");
                            System.out.println("user response: "+userDetails.getJSONObject(0).getString("latitude"));

                            JSONArray storeDetails = new JSONArray();
                            storeDetails = response.getJSONArray("storeDetails");
                            System.out.println("Store response: "+storeDetails.getJSONObject(0).getString("store_name"));
                            String store_name = storeDetails.getJSONObject(0).getString("store_name");
                            String store_address = storeDetails.getJSONObject(0).getString("store_address");
                            String distance_traveled_by_user = storeDetails.getJSONObject(0).getString("distance_traveled_by_user");
                            s.setText(store_name);
                            distance.setText(distance_traveled_by_user);
                            
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response Error", error.toString());
                Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("storeName", storeName);
                return params;
            }

        };

        VolleyController.getInstance(getActivity().getApplicationContext()).addToRequestQueue(rq);


    }

    }
