package com.nucleosystechnologies.ofconline.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.AddListAdapter;
import com.nucleosystechnologies.ofconline.Adapter.HomePagerSlider;
import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdvertiseListing extends AppCompatActivity {

    ArrayList<Addvertise_model> AddData;
    ListView addlist;
    AppSharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise_listing);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        AddData =  new ArrayList<>();
        addlist = (ListView)findViewById(R.id.addlist);
        sharedPreferences =  new AppSharedPreferences(this);
        loadadd();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void loadadd()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.GETADDBYID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    Addvertise_model model =  new Addvertise_model();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setAdd_name(jsonArray.getJSONObject(i).getString("name"));
                                    model.setImg(jsonArray.getJSONObject(i).getString("image"));

                                    AddData.add(model);
                                }

                                AddListAdapter addListAdapter = new AddListAdapter(getApplicationContext(),AddData);

                                addlist.setAdapter(addListAdapter);


                            }
                            else if (obj.getString("status").equals("400"))
                            {

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //  Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("emp_id",sharedPreferences.pref.getString(sharedPreferences.mast_id,""));

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
