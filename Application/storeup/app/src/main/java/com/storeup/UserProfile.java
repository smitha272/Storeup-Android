package com.storeup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;


public class UserProfile extends Fragment {
    AppSessionManager appSessionManager;
    private static String KEY_SUCCESS = "success";
    private RecyclerView userRecyclerView;
    //private UserInfoAdapter userInfoAdapter;
    //private List<UserProfileData> completeUserData;

    private TextView mEmail;
    private TextView uName;
    private TextView uStreet;
    private TextView uCity;
    private TextView uState;
    private TextView uZip;
    private TextView uPhone;
    private FloatingActionButton mEditProfileBtn;
    private Bundle dataBundle = new Bundle();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.user_profile_fragment,container, false);
        appSessionManager=new AppSessionManager(getActivity().getApplicationContext());

/*        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        userRecyclerView = (RecyclerView) getView().findViewById(R.id.profile_list);
        userRecyclerView.setLayoutManager(layoutManager);
        userRecyclerView.setHasFixedSize(true);


        completeUserData = new ArrayList<UserProfileData>();*/

        mEmail=(TextView)view.findViewById(R.id.email);
        uName=(TextView)view.findViewById(R.id.user_name);
        uStreet=(TextView)view.findViewById(R.id.street);
        uCity = (TextView)view.findViewById(R.id.city);
        uState = (TextView)view.findViewById(R.id.state);
        uZip = (TextView)view.findViewById(R.id.zipcode);
        uPhone = (TextView)view.findViewById(R.id.phone);

        mEditProfileBtn=(FloatingActionButton) view.findViewById(R.id.edit_profile);

        mEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment userEdit = new ProfileEdit();
                userEdit.setArguments(dataBundle);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_main,userEdit).commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Profile");

        getUserDetails();
    }
    private void getUserDetails() {
        final String email = appSessionManager.getKeyEmail();
        final String URL = "http://10.0.2.2:3000/users/userDetails" + "?email=" + email;

        // pass second argument as "null" for GET requests

        CustomJSONObjectRequest req = new CustomJSONObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            System.out.println("Response Object" + response);
                            JSONObject userDetails = new JSONObject();
                            userDetails = response.getJSONObject("user_details");
                            String email_id = userDetails.getString("email");
                            String user_name = userDetails.getString("user_name");
                            String user_pass = userDetails.getString("password");
                            String user_street = userDetails.getString("street");
                            String user_city = userDetails.getString("city");
                            String user_state = userDetails.getString("state");
                            String user_phone = userDetails.getString("phone_number");
                            String user_zip = userDetails.getString("zipcode");
                            String message = response.getString("message");



                            mEmail.setText(email_id);
                            uName.setText(user_name);
                            uStreet.setText(user_street);
                            uCity.setText(user_city);
                            uState.setText(user_state);
                            uZip.setText(user_zip);
                            uPhone.setText(user_phone);


                            dataBundle.putString("email",email_id);
                            dataBundle.putString("user_name",user_name);
                            dataBundle.putString("user_street",user_street);
                            dataBundle.putString("user_city",user_city);
                            dataBundle.putString("user_state",user_state);
                            dataBundle.putString("user_phone",user_phone);
                            dataBundle.putString("user_zip",user_zip);





                            System.out.println("Response Object" + userDetails.getString("user_id"));

                            if (response.getString(KEY_SUCCESS) != null) {
                                int success = Integer.parseInt(response.getString(KEY_SUCCESS));
                                if (success == 1) {
                                    //Toast.makeText(getActivity().getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
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
                //Toast.makeText(getActivity().getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyController.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req);
    }

}
