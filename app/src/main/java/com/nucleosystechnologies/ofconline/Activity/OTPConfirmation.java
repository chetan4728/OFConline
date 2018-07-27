package com.nucleosystechnologies.ofconline.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleosystechnologies.ofconline.R;

public class OTPConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpconfirmation);
        ActionBar actionBar = getSupportActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }


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
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("The OTP Will expire in " + millisUntilFinished / 1000 +" Seconds");
                resend.setVisibility(View.GONE);
            }

            public void onFinish() {
                timer.setText("Oops! Looks like your OTP has expired or not received");
                timer.setTextColor(getResources().getColor(R.color.redc));
                resend.setVisibility(View.VISIBLE);
            }
        }.start();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer.setText("The OTP Will expire in " + millisUntilFinished / 1000 +" Seconds");
                        resend.setVisibility(View.GONE);
                        timer.setTextColor(getResources().getColor(R.color.black_overlay));
                    }

                    public void onFinish() {
                        timer.setText("Oops! Looks like your OTP has expired or not received");
                        timer.setTextColor(getResources().getColor(R.color.redc));
                        resend.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        });

        TextView showmobile = (TextView)findViewById(R.id.showmobile);
        showmobile.setText(getIntent().getStringExtra("mobile"));
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
                    Intent intent =  new Intent(OTPConfirmation.this,Dashboard.class);
                    startActivity(intent);
                    finishAffinity();
                }

            }
        });


    }

}
