package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.AddListAdapter;
import com.nucleosystechnologies.ofconline.Adapter.PackageAdapter;
import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.Model.Package_model;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PackageActivity extends AppCompatActivity {

    ArrayList<Package_model> package_models;
    ListView pkage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        package_models =  new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Select Package");
        pkage = (ListView)findViewById(R.id.pkage);
        loadadd();
    }

    public void loadadd()
    {
        final ProgressDialog progressDialog = new ProgressDialog(PackageActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.get_package,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                //Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    Package_model model =  new Package_model();
                                    model.setPkg_id(jsonArray.getJSONObject(i).getInt("pkg_id"));
                                    model.setPkg_name(jsonArray.getJSONObject(i).getString("pkg_name"));
                                    model.setPkg_detail(jsonArray.getJSONObject(i).getJSONArray("pkg_detail"));
                                    model.setPkg_limit(jsonArray.getJSONObject(i).getString("pkg_limit"));
                                    model.setPkg_price(jsonArray.getJSONObject(i).getString("pkg_price"));
                                    model.setPkg_validity(jsonArray.getJSONObject(i).getString("pkg_validity"));

                                    package_models.add(model);
                                }

                                PackageAdapter packageAdapter = new PackageAdapter(getApplicationContext(),package_models);

                                pkage.setAdapter(packageAdapter);
                                progressDialog.hide();

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
                });
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
