package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.CategoryListAdapter;
import com.nucleosystechnologies.ofconline.Adapter.PackageAdapter;
import com.nucleosystechnologies.ofconline.Adapter.SpinnerAdapter;
import com.nucleosystechnologies.ofconline.Model.AddressModel;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.Model.Package_model;
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

public class UserProfile extends AppCompatActivity {

     AppSharedPreferences appSharedPreferences;
    EditText firstname,lasttname,mobile,mobile2,zipcode,address,email,email2,password;
    Spinner country,state,city;
    ArrayList<AddressModel> addressModel;
    ArrayList<CategoryModel> Datalist;
    TextView country_id,state_text,city_text;
    String Country_id,state_id,city_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Bundle bundle = getIntent().getExtras();
        appSharedPreferences =  new AppSharedPreferences(this);
        firstname = (EditText)findViewById(R.id.firstname);
        lasttname = (EditText)findViewById(R.id.lasttname);
        mobile = (EditText)findViewById(R.id.mobile);
        mobile2 = (EditText)findViewById(R.id.mobile2);
        country = (Spinner)findViewById(R.id.country);
        state = (Spinner)findViewById(R.id.state);
        city = (Spinner)findViewById(R.id.city);
        zipcode = (EditText)findViewById(R.id.zipcode);
        address = (EditText)findViewById(R.id.address);
        email = (EditText)findViewById(R.id.email);
        email2 = (EditText)findViewById(R.id.email2);
        password = (EditText)findViewById(R.id.password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Please Update Your Profile");

        Button udpatebtn = (Button)findViewById(R.id.udpatebtn);

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_id = (TextView)view.findViewById(R.id.cat_name);
                Country_id = String.valueOf(country_id.getTag());
                State(Country_id);
                //Toast.makeText(UserProfile.this, ""+Country_id, Toast.LENGTH_SHORT).show();
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

                //Toast.makeText(UserProfile.this, ""+Country_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });





        udpatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(firstname.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Firstname", Toast.LENGTH_SHORT).show();
                }
                else    if(lasttname.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Lastname", Toast.LENGTH_SHORT).show();
                }
                else    if(mobile.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
                }



                else    if(email.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }

                else    if(password.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatedata();
                }

            }
        });
        country();
        getuserdata();
    }
    public void getuserdata()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getuser,
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
                                    password.setText(jsonArray.getJSONObject(i).getString("user_pass"));
                                    firstname.setText(jsonArray.getJSONObject(i).getString("first_name"));
                                    lasttname.setText(jsonArray.getJSONObject(i).getString("last_name"));
                                    mobile.setText(jsonArray.getJSONObject(i).getString("mobile"));
                                    mobile2.setText(jsonArray.getJSONObject(i).getString("alt_contact"));
                                    email.setText(jsonArray.getJSONObject(i).getString("user_email"));
                                    email2.setText(jsonArray.getJSONObject(i).getString("alt_email"));
                                    zipcode.setText(jsonArray.getJSONObject(i).getString("zipcode"));
                                    address.setText(jsonArray.getJSONObject(i).getString("adrs"));



                                }



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
                params.put("mast_id",appSharedPreferences.pref.getString(AppSharedPreferences.mast_id,""));

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void updatedata()
    {
      //  Toast.makeText(this, "sdfs", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.Updateuser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(UserProfile.this, "Profile updateed Successfully", Toast.LENGTH_SHORT).show();
                        appSharedPreferences.editor.putString(appSharedPreferences.isSignup,"1");
                        appSharedPreferences.editor.commit();
                        Intent intent = new Intent(UserProfile.this, SellerDashboard.class);
                        startActivity(intent);
                        finish();
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
                params.put("first_name",firstname.getText().toString());
                params.put("last_name",lasttname.getText().toString());
                params.put("user_email",email.getText().toString());
                params.put("alt_email",email2.getText().toString());
                params.put("mobile",mobile.getText().toString());
                params.put("alt_contact",mobile2.getText().toString());
                params.put("user_pass",password.getText().toString());
                params.put("adrs",address.getText().toString());
                params.put("zipcode",zipcode.getText().toString());
                params.put("country",Country_id.toString());
                params.put("state",state_id.toString());
                params.put("city",city_id.toString());
                params.put("mast_id",appSharedPreferences.pref.getString(appSharedPreferences.mast_id,""));

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }

    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
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
