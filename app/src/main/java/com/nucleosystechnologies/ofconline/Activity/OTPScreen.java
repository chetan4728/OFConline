package com.nucleosystechnologies.ofconline.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CCPCountry;
import com.hbb20.CountryCodePicker;
import com.nucleosystechnologies.ofconline.R;

public class OTPScreen extends AppCompatActivity {
  EditText Mobile_number;
    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        Mobile_number  = (EditText)findViewById(R.id.otpmobile);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
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

        Button OtpAlert =  (Button)findViewById(R.id.next);
        OtpAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Mobile_number.getText().toString().isEmpty())
                {
                    Toast.makeText(OTPScreen.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
               else if(Mobile_number.getText().length()!=10)
                {
                    Toast.makeText(OTPScreen.this, "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    OtpAlert();
                }

            }
        });
    }


    public void OtpAlert()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);



        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.otp_confrmation, null);
        builder.setCancelable(false);
        builder.setView(promptsView);
        final AlertDialog dialog = builder.create();
        TextView mobilenumber = (TextView)promptsView.findViewById(R.id.mobilenumber);
        mobilenumber.setText(ccp.getDefaultCountryCodeWithPlus()+" "+Mobile_number.getText());

        Button editbtn = (Button)promptsView.findViewById(R.id.editbtn);
        Button confirm = (Button)promptsView.findViewById(R.id.confirm);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(OTPScreen.this,OTPConfirmation.class);
                Bundle bundle =  new Bundle();
                bundle.putString("mobile",ccp.getDefaultCountryCodeWithPlus()+" "+Mobile_number.getText());
                bundle.putString("mobile_number", String.valueOf(Mobile_number.getText()));
                intent.putExtras(bundle);
                startActivity(intent);
                dialog.dismiss();

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
}
