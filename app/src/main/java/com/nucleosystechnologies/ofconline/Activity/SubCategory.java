package com.nucleosystechnologies.ofconline.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.SubCategoryAdapter;
import com.nucleosystechnologies.ofconline.Model.SubCategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategory extends AppCompatActivity {
    ArrayList<SubCategoryModel> Datalist;
     ListView subcategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(getIntent().getExtras().getString("cate_name"));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 100);

            }
        }
        final int category_id = getIntent().getExtras().getInt("Category_id");

        final String cat_id = String.valueOf(category_id).toString();

       // Toast.makeText(this, ""+cat_id, Toast.LENGTH_SHORT).show();

        subcategory = (ListView)findViewById(R.id.subcategory);



        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading Data ...");
        pDialog.show();

        EditText serach =  (EditText)findViewById(R.id.serach);
        serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubCategory.this,SerachCategory.class);
                startActivity(i);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });
        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                         Log.i("reponse",response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    SubCategoryModel model =  new SubCategoryModel();
                                    model.setMast_id(jsonArray.getJSONObject(i).getInt("mast_id"));
                                    model.setFirst_name(jsonArray.getJSONObject(i).getString("first_name"));
                                    model.setLast_name(jsonArray.getJSONObject(i).getString("last_name"));
                                    model.setAdrs(jsonArray.getJSONObject(i).getString("adrs"));
                                    model.setCountry(jsonArray.getJSONObject(i).getString("country_name"));
                                    model.setState(jsonArray.getJSONObject(i).getString("state"));
                                    model.setCity(jsonArray.getJSONObject(i).getString("city"));
                                    model.setZipcode(jsonArray.getJSONObject(i).getString("zipcode"));
                                    model.setImg_upload(jsonArray.getJSONObject(i).getString("user_profile"));
                                    model.setMobile(jsonArray.getJSONObject(i).getString("mobile"));
                                    model.setCategory_name(jsonArray.getJSONObject(i).getString("name"));

                                    Datalist.add(model);
                                }

                                SubCategoryAdapter subCategoryAdapter =  new SubCategoryAdapter(getApplicationContext(),Datalist,SubCategory.this);
                                subcategory.setAdapter(subCategoryAdapter);

                                pDialog.hide();
                            }
                            else if (obj.getString("status").equals("400"))
                            {

                                Toast.makeText(SubCategory.this, "No data found", Toast.LENGTH_SHORT).show();
                                pDialog.hide();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(SubCategory.this, "No data found", Toast.LENGTH_SHORT).show();
                            pDialog.hide();
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
                params.put("category_id", cat_id);
                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
