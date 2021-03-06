package com.storeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.storeup.Entity.AdminAnalyticsDetails;
import com.storeup.Extras.CustomJSONObjectRequest;
import com.storeup.Extras.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class AdminActivity extends AppCompatActivity {

    private EditText city_name;
    private EditText store_name;
    private Button logout;
    private Button submit;
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        city_name = (EditText) findViewById(R.id.cityName);
        store_name = (EditText) findViewById(R.id.storeName);
        submit = (Button)findViewById(R.id.getAnalytics);
        logout = (Button)findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(AdminActivity.this,LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String storeName = store_name.getText().toString();
                String cityName = city_name.getText().toString();
                String URL = "http://3b8fd6c8.ngrok.io/getRecommendations?store="+storeName+"&city="+cityName;
                System.out.println("Reached 1111"+storeName+" "+cityName);

                CustomJSONObjectRequest req = new CustomJSONObjectRequest(URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(response.length()==0 || response==null){
                                    Toast.makeText(getApplicationContext(), "There are No suggestions for this location", Toast.LENGTH_SHORT).show();
                                }else {
                                    try {
                                        ArrayList<ArrayList<String>> arr=new ArrayList<>();
                                        Iterator iter = response.keys();
                                        String key = null;
                                        while (iter.hasNext()) {
                                            key = (String) iter.next();
//                                            list.add(new AdminAnalyticsDetails(key,response.getString(key)));
                                            list.add(key);
                                            list.add(response.getString(key));
                                            arr.add(list);
                                        }

                                        Intent home = new Intent(AdminActivity.this, AdminAnalytics.class);
                                        home.putExtra("Latitude",key);
//                                        home.putStringArrayListExtra("List", arr);
                                        home.putExtra("jsonObj", response.toString());
                                        home.putExtra("Long",response.getString(key));
                                        startActivity(home);

                                        System.out.println("The array is: "+arr);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response Error", error.toString());
                    }
                });
                req.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                VolleyController.getInstance(getApplicationContext()).addToRequestQueue(req);


            }
        });

    }
}
