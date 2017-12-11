package com.storeup;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.storeup.Adapters.UserCouponAdapter;
import com.storeup.Adapters.UserHomeAdapter;
import com.storeup.Entity.UserCouponDetails;
import com.storeup.Entity.UserHomeDetails;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {
    AppSessionManager appSessionManager;
    private static String KEY_SUCCESS = "success";
    private ListView list_view;

    private Bundle listBundle = new Bundle();

    private ArrayList<UserHomeDetails> homeDetails = new ArrayList<UserHomeDetails>();

    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
        getHomeDetails();
        list_view=(ListView)view.findViewById(R.id.storesCount);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,int position, long id) {
                listBundle.putString("user_email",appSessionManager.getKeyEmail());
                listBundle.putString("store_name",homeDetails.get(position).getStore());
                Fragment fragment = new store_details();
                fragment.setArguments(listBundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main,fragment).commit();
            }
        });
    }

    private void getHomeDetails(){
        appSessionManager=new AppSessionManager(getActivity().getApplicationContext());
        String email = appSessionManager.getKeyEmail();
        final String URL = "https://storeup-server.herokuapp.com/coupons/getCoupon" + "?email=" + email;

        CustomJSONObjectRequest req = new CustomJSONObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //System.out.println("Response Object" + response.getString("couponCount"));

                            if(response.getString("success").equals("1")) {
                                System.out.println("success==1");
                                JSONArray couponArray = response.getJSONArray("couponCount");

                                for (int i=0;i<couponArray.length();i++) {
                                    JSONObject eachStore = couponArray.getJSONObject(i);
                                    String store_name = eachStore.getString("store_name");
                                    String coupon_count = eachStore.getString("Count");
                                    System.out.println("Store: " + store_name);
                                    System.out.println("Number of coupons: " + coupon_count);
                                    if(eachStore.getString("store_name").equals("Costco")){
                                        homeDetails.add(new UserHomeDetails( eachStore.getString("store_name"), coupon_count, R.drawable.costco, true));
                                    }else if(eachStore.getString("store_name").equals("Walmart")){
                                        homeDetails.add(new UserHomeDetails( eachStore.getString("store_name"), coupon_count, R.drawable.walmart, true));
                                    }else if(eachStore.getString("store_name").equals("Target")){
                                        homeDetails.add(new UserHomeDetails( eachStore.getString("store_name"), coupon_count, R.drawable.target, true));
                                    }else {
                                        homeDetails.add(new UserHomeDetails(eachStore.getString("store_name"), coupon_count, R.drawable.store, true));
                                    }
                                }

                                UserHomeAdapter flavorAdapter = new UserHomeAdapter(getActivity(), homeDetails);

                                ListView listView = (ListView) getActivity().findViewById(R.id.storesCount);
                                listView.setAdapter(flavorAdapter);
                            }

                        } catch (JSONException e) {
                            System.out.print("Comes here 1");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response Error", error.toString());
                Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyController.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req);

    }

}
