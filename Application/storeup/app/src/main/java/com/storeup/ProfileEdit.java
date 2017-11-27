package com.storeup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEdit extends Fragment {

    private TextView uEmail;
    private TextView uName;
    private TextView uStreet;
    private TextView uCity;
    private TextView uState;
    private TextView uZip;
    private TextView uPhone;
    private Button updateProfile;
    private String url = "http://10.0.2.2:3000/users/updateUserProfile";


    public ProfileEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        Bundle bundle = getArguments();

        String email = bundle.getString("email");
        String user_name = bundle.getString("user_name");
        String user_street = bundle.getString("user_street");
        String user_city = bundle.getString("user_city");
        String user_state = bundle.getString("user_state");
        String user_zip = bundle.getString("user_zip");
        String user_phone = bundle.getString("user_phone");


        uEmail=(TextView)view.findViewById(R.id.email);
        uName=(TextView)view.findViewById(R.id.user_name);
        uStreet=(TextView)view.findViewById(R.id.street);
        uCity = (TextView)view.findViewById(R.id.city);
        uState = (TextView)view.findViewById(R.id.state);
        uZip = (TextView)view.findViewById(R.id.zip);
        uPhone = (TextView)view.findViewById(R.id.phone);

        uEmail.setText(email);
        uName.setText(user_name);
        uStreet.setText(user_street);
        uCity.setText(user_city);
        uState.setText(user_state);
        uZip.setText(user_zip);
        uPhone.setText(user_phone);

        //Toast.makeText(getActivity().getApplicationContext(), uEmail.getText(),Toast.LENGTH_LONG).show();

        updateProfile = (Button)view.findViewById(R.id.save_profile_btn);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile(uEmail.getText().toString(),uName.getText().toString(),uStreet.getText().toString(),uCity.getText().toString(),uState.getText().toString(),uZip.getText().toString(),uPhone.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");



    }

    public void updateUserProfile(final String userEmail, final String userName, final String userStreet, final String userCity, final String userState, final String userZip, final String userPhone){
       // Toast.makeText(getActivity().getApplicationContext(), userEmail+" "+userState,Toast.LENGTH_LONG).show();
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response") != null) {
                                int success = Integer.parseInt(response.getString("response"));
                                if (success == 1) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Updated Successfully",Toast.LENGTH_LONG).show();
                                } else if (success == 0) {
                                    //Toast.makeText(getApplicationContext(), R.string.invalid_login, Toast.LENGTH_LONG).show();
                                } else {
                                    //Toast.makeText(getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
                                }
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
                params.put("email", userEmail);
                params.put("userName", userName);
                params.put("userStreet", userStreet);
                params.put("userCity", userCity);
                params.put("userState", userState);
                params.put("userZip", userZip);
                params.put("userPhone", userPhone);
                return params;
            }

        };

        VolleyController.getInstance(getActivity().getApplicationContext()).addToRequestQueue(rq);


    }

}
