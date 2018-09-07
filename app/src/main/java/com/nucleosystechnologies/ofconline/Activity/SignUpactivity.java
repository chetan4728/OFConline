package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.CategoryAdapter;
import com.nucleosystechnologies.ofconline.Adapter.CategoryListAdapter;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpactivity extends AppCompatActivity {

    EditText firstname,lastname,email,mobile,password;
    Spinner category;
    Button submit;
    TextView  cat_id;
    ArrayList<CategoryModel> Datalist;
    ArrayList<String> CategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upactivity);
        firstname = (EditText)findViewById(R.id.firstname);
        lastname = (EditText)findViewById(R.id.lastname);
        email = (EditText)findViewById(R.id.email);
        mobile = (EditText)findViewById(R.id.mobile);
        password = (EditText)findViewById(R.id.password);
        category = (Spinner) findViewById(R.id.category);
        submit = (Button) findViewById(R.id.submit);
        CategoryList =  new ArrayList<>();

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat_id = (TextView)view.findViewById(R.id.cat_name);
                CategoryList.add(cat_id.getText().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(firstname.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                }
                else  if(lastname.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                }

                else  if(email.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                }

                else  if(mobile.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter mobile", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmaillId(email.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                }
                else  if(password.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                }


                else
                {
                    signup();
                }

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

    }
    private boolean isValidEmaillId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public void signup() {




       // Toast.makeText(this, "validation done"+cat_id.getTag(), Toast.LENGTH_SHORT).show();



        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Creating Account...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {

                                Toast.makeText(SignUpactivity.this, "Your Account Created Successfully", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putString("email", String.valueOf(email.getText()));
                                bundle.putString("password", String.valueOf(password.getText()));
                                Intent i =  new Intent(SignUpactivity.this,LoginActivity.class);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                            else if (obj.getString("status").equals("201"))
                            {
                                Toast.makeText(SignUpactivity.this, "Email Already Exist", Toast.LENGTH_SHORT).show();
                            }
                            else if (obj.getString("status").equals("400"))
                            {
                                Toast.makeText(SignUpactivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            }

                            pDialog.hide();
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
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("fname", String.valueOf(firstname.getText()));
                params.put("lname", String.valueOf(lastname.getText()));
                params.put("email", String.valueOf(email.getText()));
                params.put("password", String.valueOf(password.getText()));
                params.put("mobile", String.valueOf(mobile.getText()));
                params.put("category", String.valueOf(cat_id.getTag()));
                return params;
            }
        };
        VolllyRequest.getInstance(this).addToRequestQueue(stringRequest);

    }
}
