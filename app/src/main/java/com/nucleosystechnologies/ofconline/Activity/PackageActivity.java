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
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PackageActivity extends AppCompatActivity {

    ArrayList<Package_model> package_models;
    ListView pkage;
    Button plan1,plan2;
    String Price1,Price2;
    String home_aid_string,inner_aid_string;
    AppSharedPreferences appSharedPreferences;
    Card card;
    RadioButton homepck,innerpack;
    Stripe stripe;
    Integer amount;
    String finalprice;
    String name;
    Token tok;
    String reminder_flag,pacakgeName,Reminder;
    WebView webView;
    private WebSettings webSettings;
    CardInputWidget mCardInputWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        package_models =  new ArrayList<>();
        appSharedPreferences = new AppSharedPreferences(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Select Package");
        homepck = (RadioButton)findViewById(R.id.homepck);
        innerpack = (RadioButton)findViewById(R.id.innerpack);

        getuserdata();
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

               // Toast.makeText(PackageActivity.this, ""+finalprice, Toast.LENGTH_SHORT).show();
                if(Reminder==null)
                {
                    Intent  i =  new Intent(PackageActivity.this,PaymentActivity.class);
                    Bundle bundle =  new Bundle();
                    bundle.putString("price",finalprice);
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else
                {
                    int catalog_outdated = 0;
                    String valid_until = Reminder;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(valid_until);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (new Date().after(strDate)) {
                        catalog_outdated = 1;
                    }

                    if(catalog_outdated==1) {
                        // Toast.makeText(PackageActivity.this, ""+Price2, Toast.LENGTH_SHORT).show();
                        Intent  i =  new Intent(PackageActivity.this,PaymentActivity.class);
                        Bundle bundle =  new Bundle();
                        bundle.putString("price",finalprice);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(PackageActivity.this, "You currently on "+pacakgeName+" will expire on "+Reminder, Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
        plan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Reminder==null)
                {
                    Intent  i =  new Intent(PackageActivity.this,PaymentActivity.class);
                    Bundle bundle =  new Bundle();
                    bundle.putString("price",finalprice);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else
                {
                    int catalog_outdated = 0;
                    String valid_until = Reminder;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(valid_until);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (new Date().after(strDate)) {
                        catalog_outdated = 1;
                    }

                    if(catalog_outdated==1) {
                        // Toast.makeText(PackageActivity.this, ""+Price2, Toast.LENGTH_SHORT).show();
                        Intent  i =  new Intent(PackageActivity.this,PaymentActivity.class);
                        Bundle bundle =  new Bundle();
                        bundle.putString("price",Price2);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(PackageActivity.this, "You currently on "+pacakgeName+" will expire on "+Reminder, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }



    public void getuserdata()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.GETTRANS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("response",response);

                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {

                                    TextView  current_package = (TextView)findViewById(R.id.current_package);
                                    TextView  reminder = (TextView)findViewById(R.id.reminder);

                                    current_package.setText("Your Currently Selected Package: "+jsonArray.getJSONObject(i).getString("package_selected")
                                );
                                    reminder.setText("Package Renewal Date: "+jsonArray.getJSONObject(i).getString("aid_renewal")
                                    );

                                    reminder_flag = jsonArray.getJSONObject(i).getString("remainder_flag");
                                    pacakgeName=  jsonArray.getJSONObject(i).getString("package_selected");
                                    Reminder = jsonArray.getJSONObject(i).getString("remainder_date");
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





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
