package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.AppController;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ImageView loginbtn = (ImageView)findViewById(R.id.loginbtn);

        final EditText email = (EditText)findViewById(R.id.email);
        final EditText password = (EditText)findViewById(R.id.password);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString().trim().isEmpty())
                {

                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    login();
                }


            }
        });


    }


    public void login()
    {

        final EditText email = (EditText)findViewById(R.id.email);
        final EditText password = (EditText)findViewById(R.id.password);
        String  tag_string_req = "string_req";



        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Logging in ...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                             if(obj.getString("status").equals("200"))
                             {


                                 JSONArray jsonArray = obj.getJSONArray("data");
                                 Bundle bundle = new Bundle();
                                 bundle.putString("Array",jsonArray.toString());
                                 Intent i =  new Intent(LoginActivity.this,Dashboard.class);
                                 i.putExtras(bundle);
                                 startActivity(i);
                             }
                             else if (obj.getString("status").equals("400"))
                             {
                                 Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
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
                params.put("user_email", String.valueOf(email.getText()));
                params.put("user_pass", String.valueOf(password.getText()));
                return params;
            }
        };
        VolllyRequest.getInstance(this).addToRequestQueue(stringRequest);

    }
}
