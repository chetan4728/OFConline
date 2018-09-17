package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.CategoryListAdapter;
import com.nucleosystechnologies.ofconline.Adapter.SpinnerAdapter;
import com.nucleosystechnologies.ofconline.Adapter.SubCategoryAdapter;
import com.nucleosystechnologies.ofconline.Model.AddressModel;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
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

public class SerachCategory extends AppCompatActivity {
    Spinner country,state,city;
    Spinner category;
    ArrayList<AddressModel> addressModel;
    ArrayList<CategoryModel> Datalist;
    String Country_id,state_id,city_id,Cat_id;
    ArrayList<SubCategoryModel> Datalistsecond;
    ListView searchresult;
    TextView country_id,state_text,city_text,cat_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach_category);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Search Services And More");
        country = (Spinner)findViewById(R.id.country);
        state = (Spinner)findViewById(R.id.state);
        searchresult = (ListView)findViewById(R.id.searchresult);
        city = (Spinner)findViewById(R.id.city);
        category = (Spinner) findViewById(R.id.category);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat_id = (TextView)view.findViewById(R.id.cat_name);

                if(position >0)
                {
                    serach();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_id = (TextView)view.findViewById(R.id.cat_name);
                Country_id = String.valueOf(country_id.getTag());
                State(Country_id);
                if(position >0)
                {
                    serach();
                }
             //   Toast.makeText(SerachCategory.this, ""+Country_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_text = (TextView)view.findViewById(R.id.cat_name);
                state_id = String.valueOf(state_text.getTag());
                city(state_id);
                if(position >0)
                {
                    serach();
                }
              //  Toast.makeText(SerachCategory.this, ""+state_id, Toast.LENGTH_SHORT).show();
                //Toast.makeText(UserProfile.this, ""+Country_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_text = (TextView)view.findViewById(R.id.cat_name);
                city_id = String.valueOf(city_text.getTag());

                if(position >0)
                {
                    serach();
                }
               // Toast.makeText(SerachCategory.this, ""+city_id, Toast.LENGTH_SHORT).show();
                //Toast.makeText(UserProfile.this, ""+Country_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    CategoryModel model =  new CategoryModel();
                                    model.setCategory_id(jsonArray.getJSONObject(i).getInt("category_id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    model.setDescription(jsonArray.getJSONObject(i).getString("description"));
                                    model.setImg(jsonArray.getJSONObject(i).getString("img"));
                                    Datalist.add(model);
                                }
                                CategoryListAdapter categoryListAdapter =  new CategoryListAdapter(getApplicationContext(),R.layout.category_spinner_item,Datalist);
                                category.setAdapter(categoryListAdapter);
                                // categoryList.setAdapter(categoryAdapter);


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
        country();
    }

    public void serach()
    {
        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading Data ...");
        pDialog.show();


        Datalistsecond = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SERACH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
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
                                    Datalistsecond.add(model);
                                }


                                SubCategoryAdapter subCategoryAdapter =  new SubCategoryAdapter(getApplicationContext(),Datalistsecond,SerachCategory.this);
                                searchresult.setAdapter(subCategoryAdapter);

                                pDialog.hide();
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
                params.put("category_id", String.valueOf(cat_id.getTag()));
                params.put("country", String.valueOf(Country_id));
                params.put("state", String.valueOf(state_id));
                params.put("city", String.valueOf(city_id));
                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    public  void country()
    {

        //  final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
        // progressDialog.setMessage("Loading...");
        //  progressDialog.show();
        //  progressDialog.setCancelable(false);
        addressModel =  new ArrayList<>();
        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.getCountry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    AddressModel model =  new AddressModel();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    addressModel.add(model);
                                }
                                SpinnerAdapter spinnerAdapter =  new SpinnerAdapter(getApplicationContext(),R.layout.category_spinner_item,addressModel);
                                country.setAdapter(spinnerAdapter);
                                // categoryList.setAdapter(categoryAdapter);
                                //progressDialog.hide();

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

    public  void State(final String id)
    {


        addressModel =  new ArrayList<>();
        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.statelist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    AddressModel model =  new AddressModel();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    addressModel.add(model);
                                }
                                SpinnerAdapter spinnerAdapter =  new SpinnerAdapter(getApplicationContext(),R.layout.category_spinner_item,addressModel);
                                state.setAdapter(spinnerAdapter);
                                // categoryList.setAdapter(categoryAdapter);


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
                params.put("country_id",id);


                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public  void city(final String id)
    {


        addressModel =  new ArrayList<>();
        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.city,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    AddressModel model =  new AddressModel();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    addressModel.add(model);
                                }
                                SpinnerAdapter spinnerAdapter =  new SpinnerAdapter(getApplicationContext(),R.layout.category_spinner_item,addressModel);
                                city.setAdapter(spinnerAdapter);
                                // categoryList.setAdapter(categoryAdapter);


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
                params.put("state_id",id);

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
