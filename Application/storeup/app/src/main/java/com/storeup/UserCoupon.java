package com.storeup;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.storeup.Adapters.UserCouponAdapter;
import com.storeup.Entity.UserCouponDetails;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCoupon extends Fragment {
    AppSessionManager appSessionManager;
    private static String KEY_SUCCESS = "success";

    private ArrayList<UserCouponDetails> couponDetails = new ArrayList<UserCouponDetails>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_coupon_fragment,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Coupon");
        getUserReceipts();
    }

    private void getUserReceipts(){
        appSessionManager=new AppSessionManager(getActivity().getApplicationContext());
        String email = appSessionManager.getKeyEmail();
        final String URL = "http://10.0.2.2:3000/coupons/getCoupon" + "?email=" + email;

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
                                    if(Integer.parseInt(coupon_count) >= 10){
                                        System.out.println("Store inside if: " + store_name);
//
                                            if(Integer.parseInt(coupon_count)%10 > 0){
                                                int count = Integer.parseInt(coupon_count)/10;
                                                for(int j=0;j<count;j++){
                                                    couponDetails.add(new UserCouponDetails( eachStore.getString("store_name"), eachStore.getString("Count"), R.drawable.sad, false));
                                                }
                                            }
                                    }
                                    else{
                                        couponDetails.add(new UserCouponDetails( eachStore.getString("store_name"), "", R.drawable.sad, true));
                                    }
                                }

                                UserCouponAdapter flavorAdapter = new UserCouponAdapter(getActivity(), couponDetails);

                                ListView listView = (ListView) getActivity().findViewById(R.id.userCoupons);
                                listView.setAdapter(flavorAdapter);
                            }
                            else {
                                couponDetails.add(new UserCouponDetails(null,null,0, false));
                                UserCouponAdapter couponAdapter = new UserCouponAdapter(getActivity(), couponDetails);

                                // Get a reference to the ListView, and attach the adapter to the listView.
                                ListView listView = (ListView) getActivity().findViewById(R.id.userCoupons);
                                listView.setAdapter(couponAdapter);
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
