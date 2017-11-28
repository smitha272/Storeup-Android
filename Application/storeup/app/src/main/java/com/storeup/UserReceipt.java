package com.storeup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.storeup.Adapters.UserReceiptAdapter;
import com.storeup.Entity.UserReceiptDetails;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class UserReceipt extends Fragment {
    AppSessionManager appSessionManager;
    private ListView list_view;
    private TextView store_name;
    private TextView store_address;
    private TextView downoald_url;
    private Bundle listBundle = new Bundle();
    private static String KEY_SUCCESS = "success";

    private ArrayList<UserReceiptDetails> userReceiptDetailses = new ArrayList<UserReceiptDetails>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_receipts_fragment,container, false);

        /**/
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Scanned Receipt");
        getUserReceipts();
        list_view =(ListView)view.findViewById(R.id.listview_flavor);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),"Store Name:"+userReceiptDetailses.get(position).getStore_name(), Toast.LENGTH_LONG).show();
                listBundle.putString("store_name",userReceiptDetailses.get(position).getStore_name());
                listBundle.putString("store_address",userReceiptDetailses.get(position).getStore_address());
                listBundle.putString("download_url",userReceiptDetailses.get(position).getDownload_url());
                listBundle.putString("latitude",userReceiptDetailses.get(position).getLatitude());
                listBundle.putString("longitude",userReceiptDetailses.get(position).getLongitude());
                Fragment receipt = new Receipt_details();
                receipt.setArguments(listBundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main,receipt).commit();
                //Here you can hadle your Views....
            }
        });
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
                                    if(eachReceipt.getString("latitude").equals("0") && eachReceipt.getString("longitude").equals("0")){
                                        userReceiptDetailses.add(new UserReceiptDetails(eachReceipt.getString("store_name"), eachReceipt.getString("store_address"), eachReceipt.getString("download_url")));
                                    }else {
                                        userReceiptDetailses.add(new UserReceiptDetails(eachReceipt.getString("store_name"), eachReceipt.getString("store_address"), eachReceipt.getString("download_url"), eachReceipt.getString("latitude"), eachReceipt.getString("longitude")));
                                        System.out.println("The Longitude is"+eachReceipt.getString("longitude"));
                                    }
                                }
                                UserReceiptAdapter receiptAdapter = new UserReceiptAdapter(getActivity(), userReceiptDetailses);

                                // Get a reference to the ListView, and attach the adapter to the listView.
                                ListView listView = (ListView) getActivity().findViewById(R.id.listview_flavor);
                                listView.setAdapter(receiptAdapter);
                            }
                            else {
                                userReceiptDetailses.add(new UserReceiptDetails(null,null,null,null,null));
                                UserReceiptAdapter receiptAdapter = new UserReceiptAdapter(getActivity(), userReceiptDetailses);

                                // Get a reference to the ListView, and attach the adapter to the listView.
                                ListView listView = (ListView) getActivity().findViewById(R.id.listview_flavor);
                                listView.setAdapter(receiptAdapter);
                            }

                        } catch (JSONException e) {
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
