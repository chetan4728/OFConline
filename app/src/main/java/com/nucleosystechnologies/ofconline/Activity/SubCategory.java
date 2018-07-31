package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(getIntent().getExtras().getString("cate_name"));



        final int category_id = getIntent().getExtras().getInt("category_id");

        final String cat_id = String.valueOf(category_id).toString();



        final ListView subcategory = (ListView)findViewById(R.id.subcategory);






        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                        // Log.i("reponse",response);

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
                                    Datalist.add(model);
                                }

                                SubCategoryAdapter subCategoryAdapter =  new SubCategoryAdapter(getApplicationContext(),Datalist);
                                subcategory.setAdapter(subCategoryAdapter);


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
