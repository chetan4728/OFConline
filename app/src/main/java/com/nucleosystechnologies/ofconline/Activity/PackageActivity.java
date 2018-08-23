package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PackageActivity extends AppCompatActivity {

    ArrayList<Package_model> package_models;
    ListView pkage;
    Button plan1,plan2;
    Card card;
    Stripe stripe;
    Integer amount;
    String name;
    Token tok;

    CardInputWidget mCardInputWidget;
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
        plan1 = (Button)findViewById(R.id.plan1);
        plan2 = (Button)findViewById(R.id.paln2);
        plan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtpAlert();
            }
        });
        plan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtpAlert();
            }
        });

        amount = 500;
        name = "500";
        stripe = new Stripe(getApplicationContext(),"pk_test_AARyUyZkzPCCc060fxvm2Ond");

        //pkage = (ListView)findViewById(R.id.pkage);
     //   loadadd();
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

    public void OtpAlert()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);



        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.pack_activation_alert, null);
        builder.setCancelable(false);
        builder.setView(promptsView);
        final AlertDialog dialog = builder.create();



        Button editbtn = (Button)promptsView.findViewById(R.id.editbtn);
        Button confirm = (Button)promptsView.findViewById(R.id.confirm);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card = new Card("4242424242424242", 12, 2019, "123");

                card.setCurrency("usd");
                card.setName("Theodhor Pandeli");
                card.setAddressZip("1000");
        /*
        card.setNumber(4242424242424242);
        card.setExpMonth(12);
        card.setExpYear(19);
        card.setCVC("123");
        */


                stripe.createToken(card, "pk_test_AARyUyZkzPCCc060fxvm2Ond", new TokenCallback() {
                    public void onSuccess(Token token) {
                        // TODO: Send Token information to your backend to initiate a charge
                        Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                        tok = token;
                        verify(token.getId());

                    }

                    public void onError(Exception error) {
                        Log.d("Stripe", error.getLocalizedMessage());
                    }
                });

            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
// Set up the buttons

        dialog.show();
    }


    public void verify(final String token)
    {





        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://ofconline.in/subscription",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                         Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
                params.put("stripeToken",token);
                params.put("price", String.valueOf("50"));

                params.put("pack_val", String.valueOf("50"));
                params.put("pack_person", String.valueOf("1"));
                return params;
            }
        };
        VolllyRequest.getInstance(this).addToRequestQueue(stringRequest);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
