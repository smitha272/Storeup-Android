package com.storeup;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.storeup.Adapters.UserInfoAdapter;
import com.storeup.Adapters.UserReceiptAdapter;
import com.storeup.Entity.UserProfileData;
import com.storeup.Entity.UserReceiptData;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserReceipt extends Fragment {
    AppSessionManager appSessionManager;
    private static String KEY_SUCCESS = "success";
    private RecyclerView userReceiptRecyclerView;
    private UserReceiptAdapter userReceiptAdapter;
    private List<UserReceiptData> completeReceiptData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_receipts_fragment,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Scanned Receipt");
        appSessionManager=new AppSessionManager(getActivity().getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        userReceiptRecyclerView = (RecyclerView) getView().findViewById(R.id.receipt_list);
        userReceiptRecyclerView.setLayoutManager(layoutManager);
        userReceiptRecyclerView.setHasFixedSize(true);


        completeReceiptData = new ArrayList<UserReceiptData>();
        getUserReceipts();
    }

    private void getUserReceipts() {
        String email = appSessionManager.getKeyEmail();
        final String URL = "http://10.0.2.2:3000/users/userReceipts" + "?email=" + email;

        CustomJSONObjectRequest req = new CustomJSONObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.print("Comes here");
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            JSONArray receiptArray = response.getJSONArray("receipts");
//                            System.out.println("Response Object" + receiptArray.get(0));
                            JSONObject myreceipt = receiptArray.getJSONObject(0);
                            System.out.println("Response Object" + receiptArray);

                            for (int i=0;i<receiptArray.length();i++) {
                                JSONObject eachReceipt = receiptArray.getJSONObject(i);
                                completeReceiptData.add(new UserReceiptData(eachReceipt.getInt("receipt_id"), eachReceipt.getString("store_name"),eachReceipt.getString("store_address"), eachReceipt.getString("download_url"), eachReceipt.getString("distance_traveled_by_user")));

                                userReceiptAdapter = new UserReceiptAdapter(getActivity(), completeReceiptData);
                                userReceiptRecyclerView.setAdapter(userReceiptAdapter);
                            }

//                            JSONObject userDetails = new JSONObject();
//                            userDetails = response.getJSONObject("user_details");
//                            String user_phone = userDetails.getString("phone_number");
//                            String user_zip = userDetails.getString("zipcode");
//
//                            completeUserData.add(new UserProfileData(user_name, user_pass, user_street, user_city, user_state, user_phone, user_zip));
//
//                            userInfoAdapter = new UserInfoAdapter(getActivity(), completeUserData);
//                            userRecyclerView.setAdapter(userInfoAdapter);
//
//                            System.out.println("Response Object" + userDetails.getString("user_id"));

//                            if (response.getString(KEY_SUCCESS) != null) {
//                                int success = Integer.parseInt(response.getString(KEY_SUCCESS));
//                                if (success == 1) {
//                                    Toast.makeText(getActivity().getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
//                                }
//                                else {
//                                    Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
//                                }
//                            }
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

        Toast.makeText(getActivity().getApplicationContext(),URL, Toast.LENGTH_LONG).show();

    }

}
