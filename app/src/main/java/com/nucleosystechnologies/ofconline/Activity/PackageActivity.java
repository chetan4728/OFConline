package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PackageActivity extends AppCompatActivity {

    ArrayList<Package_model> package_models;
    ListView pkage;
    Button plan1,plan2;
    String Price1,Price2;
    String home_aid_string,inner_aid_string;
    Card card;
    RadioButton homepck,innerpack;
    Stripe stripe;
    Integer amount;
    String finalprice;
    String name;
    Token tok;
    WebView webView;
    private WebSettings webSettings;
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
        homepck = (RadioButton)findViewById(R.id.homepck);
        innerpack = (RadioButton)findViewById(R.id.innerpack);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://ofconline.in/Payment/aidpan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");
                                home_aid_string  = jsonArray.getJSONObject(0).getString("home_aid");
                                inner_aid_string  = jsonArray.getJSONObject(0).getString("inner_aid");
                                homepck.setTag(home_aid_string);
                                innerpack.setTag(inner_aid_string);

                                homepck.setChecked(true);


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






        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://ofconline.in/Payment/bussiness_promotion",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");
                                Price2  = jsonArray.getJSONObject(0).getString("plan_price");






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
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);

        RadioGroup rg = (RadioGroup) findViewById(R.id.allradio);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.homepck:
                        finalprice = String.valueOf(homepck.getTag());
                        // do operations specific to this selection
                        break;
                    case R.id.innerpack:
                        finalprice = String.valueOf(innerpack.getTag());
                        break;

                }
            }
        });

        plan1 = (Button)findViewById(R.id.plan1);
        plan2 = (Button)findViewById(R.id.paln2);
        plan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(PackageActivity.this, ""+finalprice, Toast.LENGTH_SHORT).show();
                Intent  i =  new Intent(PackageActivity.this,PaymentActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putString("price",finalprice);
                i.putExtras(bundle);
                startActivity(i);

            }
        });
        plan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(PackageActivity.this, ""+Price2, Toast.LENGTH_SHORT).show();
                Intent  i =  new Intent(PackageActivity.this,PaymentActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putString("price",Price2);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }









    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
