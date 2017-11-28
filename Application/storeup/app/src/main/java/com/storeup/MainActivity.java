package com.storeup;

import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.storeup.Entity.UserProfileData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener /*, View.OnClickListener*/ {
//    private String url = "http://10.0.2.2:3000/ocr/getImageOcr";
    private static String KEY_SUCCESS = "success";
    private static String KEY_USERID  = "userid";
    private RecyclerView userRecyclerView;
    //private UserInfoAdapter userInfoAdapter;
    private List<UserProfileData> completeUserData;
    String s;
    TextView test;
    AppSessionManager appSessionManager;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appSessionManager=new AppSessionManager(getApplicationContext());
        appSessionManager.checkLoginStatus();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        completeUserData = new ArrayList<UserProfileData>();

        s = appSessionManager.getUserName();
        //s= getIntent().getStringExtra("username");
        Toast.makeText(getApplicationContext(),"Welcome "+s, Toast.LENGTH_LONG).show();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedNavScreen(R.id.user_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        test =(TextView) findViewById(R.id.textView);
        test.setText(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    private void getUserDetails() {
//        String email = appSessionManager.getKeyEmail();
//        final String URL = "http://10.0.2.2:3000/users/userDetails" + "?email=" + email;
////        userRecyclerView = (RecyclerView) findViewById(R.id.profile_list);
//        // pass second argument as "null" for GET requests
//
//        CustomJSONObjectRequest req = new CustomJSONObjectRequest(URL, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            VolleyLog.v("Response:%n %s", response.toString(4));
//                            System.out.println("Response Object" + response);
//                            JSONObject userDetails = new JSONObject();
//                            userDetails = response.getJSONObject("user_details");
//                            String user_name = userDetails.getString("user_name");
//                            String user_pass = userDetails.getString("password");
//                            String user_street = userDetails.getString("street");
//                            String user_city = userDetails.getString("city");
//                            String user_state = userDetails.getString("state");
//                            String user_phone = userDetails.getString("phone_number");
//                            String user_zip = userDetails.getString("zipcode");
//                            String message = response.getString("message");
//
//                            completeUserData.add(new UserProfileData(user_name, user_pass, user_street, user_city, user_state, user_phone, user_zip));
//
//                            userInfoAdapter = new UserInfoAdapter(MainActivity.this, completeUserData);
//                            userRecyclerView.setAdapter(userInfoAdapter);
//
//                            System.out.println("Response Object" + userDetails.getString("user_id"));
//
//                            if (response.getString(KEY_SUCCESS) != null) {
//                                int success = Integer.parseInt(response.getString(KEY_SUCCESS));
//                                if (success == 1) {
//                                    Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
//                                }
//                                else {
//                                    Toast.makeText(getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Response Error", error.toString());
//                Toast.makeText(getApplicationContext(), R.string.invalid_post, Toast.LENGTH_LONG).show();
//            }
//        });
//        req.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        VolleyController.getInstance(getApplicationContext()).addToRequestQueue(req);
//
//        Toast.makeText(getApplicationContext(),URL, Toast.LENGTH_LONG).show();
//    }


    private void displaySelectedNavScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.user_home:
                fragment = new HomePage();
                break;
            case R.id.user_profile:
                fragment = new UserProfile();
                break;
            case R.id.user_receipts:
                fragment = new UserReceipt();
                break;
            case R.id.user_coupons:

                fragment = new UserCoupon();
                break;
            /*case R.id.faqs:
                Intent faqs = new Intent(MainActivity.this, nav_faqs.class);
                startActivity(faqs);
                //fragment = new Faqs();
                break;*/
            case R.id.scan_receipts:
                fragment = new ScanReceipt();
                break;
            case R.id.user_signout:
                appSessionManager.logoutUser();

        }
        if(fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.remove(fragment);
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedNavScreen(id);

        return true;
    }
}
