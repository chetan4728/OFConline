package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPConfirmation extends AppCompatActivity {

    String MobileNumber;
    EditText otpget;
    TextView hideotp;
    public static final String mypreference = "mypref";
    public static final String Mobile = "Mobile";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpconfirmation);
        hideotp = (TextView) findViewById(R.id.hideotp);
        ActionBar actionBar = getSupportActionBar();
        otpget = (EditText)findViewById(R.id.otpget);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT > 10) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;


            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        final TextView timer = (TextView)findViewById(R.id.timer);
        final Button resend = (Button)findViewById(R.id.resend);
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("The OTP Will expire in " + millisUntilFinished / 1000 +" Seconds");
                resend.setVisibility(View.GONE);
            }

            public void onFinish() {
                timer.setText("Oops! Looks like your OTP has expired or not received");
                timer.setTextColor(getResources().getColor(R.color.redc));
                resend.setVisibility(View.VISIBLE);
                expireOTP(MobileNumber);
            }
        }.start();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp(MobileNumber);
                CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer.setText("The OTP Will expire in " + millisUntilFinished / 1000 +" Seconds");
                        resend.setVisibility(View.GONE);
                        timer.setTextColor(getResources().getColor(R.color.black_overlay));
                    }

                    public void onFinish() {
                        timer.setText("Oops! Looks like your OTP has expired or not received");
                        timer.setTextColor(getResources().getColor(R.color.redc));
                        resend.setVisibility(View.VISIBLE);
                        expireOTP(MobileNumber);
                    }
                }.start();
            }
        });

        TextView showmobile = (TextView)findViewById(R.id.showmobile);
        showmobile.setText(getIntent().getStringExtra("mobile"));
        MobileNumber = getIntent().getStringExtra("mobile_number");
        sendOtp(MobileNumber);
        Button pre = (Button)findViewById(R.id.pre);
        Button next = (Button)findViewById(R.id.next);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(OTPConfirmation.this,OTPScreen.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                EditText otpget = (EditText)findViewById(R.id.otpget);
                if(otpget.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(OTPConfirmation.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    verifyOTP(MobileNumber);


                }

            }
        });


    }
    public void sendOtp(final String mobile)
    {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setMessage("Sending OTP ...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SENDOTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                               // Toast.makeText(OTPConfirmation.this, ""+obj.getString("data"), Toast.LENGTH_SHORT).show();
                              otpget.setTag(obj.getString("data"));


                            }
                            else if (obj.getString("status").equals("400"))
                            {
                                Toast.makeText(OTPConfirmation.this, "Login Failed", Toast.LENGTH_SHORT).show();
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
                params.put("number",mobile);

                return params;
            }
        };
        VolllyRequest.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void expireOTP(final String mobile)
    {





        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.EXPIRED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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
                params.put("mobile",mobile);
                params.put("otp", String.valueOf(otpget.getTag()));

                return params;
            }
        };
        VolllyRequest.getInstance(this).addToRequestQueue(stringRequest);

    }


    public void verifyOTP(final String mobile)
    {





        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.VERFYOTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(OTPConfirmation.this, ""+response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                Toast.makeText(OTPConfirmation.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent =  new Intent(OTPConfirmation.this,Dashboard.class);
                                startActivity(intent);
                                finishAffinity();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Mobile, MobileNumber);
                                editor.commit();


                            }
                            else if (obj.getString("status").equals("400"))
                            {
                                Toast.makeText(OTPConfirmation.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("mobile",mobile);
                params.put("otp", String.valueOf(otpget.getTag()));

                return params;
            }
        };
        VolllyRequest.getInstance(this).addToRequestQueue(stringRequest);

    }

}
