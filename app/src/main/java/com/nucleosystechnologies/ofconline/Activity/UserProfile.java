package com.nucleosystechnologies.ofconline.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;

public class UserProfile extends AppCompatActivity {

     AppSharedPreferences appSharedPreferences;
    EditText firstname,lasttname,mobile,mobile2,country,state,city,zipcode,address,email,email2,password;
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
        country = (EditText)findViewById(R.id.country);
        state = (EditText)findViewById(R.id.state);
        city = (EditText)findViewById(R.id.city);
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
        firstname.setText(appSharedPreferences.pref.getString("FirstName",""));
        lasttname.setText(appSharedPreferences.pref.getString("LastName",""));
        mobile.setText(appSharedPreferences.pref.getString("Mobile",""));
        email.setText(appSharedPreferences.pref.getString("email",""));
        Button udpatebtn = (Button)findViewById(R.id.udpatebtn);
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
                else    if(mobile2.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Alternate Mobile", Toast.LENGTH_SHORT).show();
                }
                else    if(country.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Select country", Toast.LENGTH_SHORT).show();
                }
                else    if(state.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Select State", Toast.LENGTH_SHORT).show();
                }
                else    if(city.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }
                else    if(email.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }
                else    if(email2.getText().toString().isEmpty())
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
    }

    public void updatedata()
    {
        Toast.makeText(this, "sdfs", Toast.LENGTH_SHORT).show();
        appSharedPreferences.editor.putString(appSharedPreferences.isSignup,"1");
        appSharedPreferences.editor.commit();
        Intent intent = new Intent(UserProfile.this, SellerDashboard.class);

        startActivity(intent);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
