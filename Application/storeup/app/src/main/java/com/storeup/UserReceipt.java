package com.storeup;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.storeup.Adapters.UserReceiptAdapter;
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
    //private UserReceiptAdapter userReceiptAdapter;
    private List<UserReceiptData> completeReceiptData;

    private ArrayList<UserReceiptDetails> userReceiptDetailses = new ArrayList<UserReceiptDetails>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_receipts_fragment,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Scanned Receipt");
        getUserReceipts();
    }

    private void getUserReceipts(){
        appSessionManager=new AppSessionManager(getActivity().getApplicationContext());
        String email = appSessionManager.getKeyEmail();
        final String URL = "http://10.0.2.2:3000/users/userReceipts" + "?email=" + email;

        CustomJSONObjectRequest req = new CustomJSONObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Response Object" + response.getString("success"));

                            if(response.getString("success").equals("1")) {
                                System.out.println("success==1");
                                JSONArray receiptArray = response.getJSONArray("receipts");

                                for (int i=0;i<receiptArray.length();i++) {
                                    JSONObject eachReceipt = receiptArray.getJSONObject(i);
                                    userReceiptDetailses.add(new UserReceiptDetails( eachReceipt.getString("store_name"), eachReceipt.getString("store_address"), eachReceipt.getString("download_url")));
                                }
                                AndroidFlavorAdapter flavorAdapter = new AndroidFlavorAdapter(getActivity(), userReceiptDetailses);

                                // Get a reference to the ListView, and attach the adapter to the listView.
                                ListView listView = (ListView) getActivity().findViewById(R.id.listview_flavor);
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
