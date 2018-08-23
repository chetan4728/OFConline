package com.nucleosystechnologies.ofconline.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nucleosystechnologies.ofconline.Adapter.CategoryListAdapter;
import com.nucleosystechnologies.ofconline.Adapter.menu_content_adapter;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.AppController;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
import com.nucleosystechnologies.ofconline.Utility.Utility;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;

public class SellerDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> MenuName;
    ArrayList<Integer> ImageList;
    ProgressDialog pDialog;
    ArrayList<CategoryModel> Datalist;
    AppSharedPreferences sharedPreferences;
    String paymentStatus;
    TextView  firstname,lasttname,mobile,mobile2,zipcode,address,email,email2,password,country,state,city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ListView MenuIcon = (ListView)findViewById(R.id.MenuIcon);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences =  new AppSharedPreferences(this);
        if(sharedPreferences.pref.getString("isSignup","").isEmpty())
        {
            Intent intent = new Intent(SellerDashboard.this, UserProfile.class);
            startActivity(intent);
        }
        Button edit = (Button)findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(SellerDashboard.this,UserProfile.class);
                startActivity(i);
            }
        });

        firstname = (TextView)findViewById(R.id.firstname);
        lasttname = (TextView)findViewById(R.id.lasttname);
        mobile = (TextView)findViewById(R.id.mobile);
        mobile2 = (TextView)findViewById(R.id.mobile2);
        country = (TextView) findViewById(R.id.country);
        state = (TextView)findViewById(R.id.state);
        city = (TextView)findViewById(R.id.city);
        zipcode = (TextView)findViewById(R.id.zipcode);
        address = (TextView)findViewById(R.id.address);
        email = (TextView)findViewById(R.id.email);
        email2 = (TextView)findViewById(R.id.email2);
        password = (TextView)findViewById(R.id.password);


        MenuName = new ArrayList<>();
        ImageList = new ArrayList<>();

        MenuName.add("Home");
        MenuName.add("Package");
        MenuName.add("Advertisement");
        MenuName.add("Setting");
        MenuName.add("Logout");
        ImageList.add(R.drawable.seller_home);
        ImageList.add(R.drawable.seller_package);
        ImageList.add(R.drawable.seller_advertiesment);
        ImageList.add(R.drawable.seller_setting);
        ImageList.add(R.drawable.seller_logout);
        pDialog = new ProgressDialog(this);
        menu_content_adapter menu_content_adapter =  new menu_content_adapter(getApplicationContext(),MenuName, ImageList);
        MenuIcon.setAdapter(menu_content_adapter);
        View header=navigationView.getHeaderView(0);
        TextView headrmobile = (TextView)header.findViewById(R.id.headrmobile);
        headrmobile.setText(sharedPreferences.pref.getString(sharedPreferences.Mobile,""));

        TextView headername = (TextView)header.findViewById(R.id.headername);
        headername.setText(sharedPreferences.pref.getString(sharedPreferences.FirstName,"")+" "+sharedPreferences.pref.getString(sharedPreferences.LastName,""));

        Datalist = new ArrayList<>();
        getuserdata();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

                                    paymentStatus  = jsonArray.getJSONObject(i).getString("package_selected");

                                    sharedPreferences.editor.putString(AppSharedPreferences.PAYMENT_STATUS,paymentStatus);
                                    sharedPreferences.editor.commit();
                                    password.setText(jsonArray.getJSONObject(i).getString("user_pass"));
                                    firstname.setText(jsonArray.getJSONObject(i).getString("first_name"));
                                    lasttname.setText(jsonArray.getJSONObject(i).getString("last_name"));
                                    mobile.setText(jsonArray.getJSONObject(i).getString("mobile"));
                                    mobile2.setText(jsonArray.getJSONObject(i).getString("alt_contact"));
                                    email.setText(jsonArray.getJSONObject(i).getString("user_email"));
                                    email2.setText(jsonArray.getJSONObject(i).getString("alt_email"));
                                    zipcode.setText(jsonArray.getJSONObject(i).getString("zipcode"));
                                    address.setText(jsonArray.getJSONObject(i).getString("adrs"));
                                    if (paymentStatus=="null")
                                    {
                                        //OtpAlert();
                                    }



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
                params.put("mast_id",sharedPreferences.pref.getString(AppSharedPreferences.mast_id,""));

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else  if (id == R.id.profile) {
            Intent intent = new Intent(SellerDashboard.this,UserProfile.class);
            Bundle bundle = new Bundle();
            bundle.putString("full_name","Hello User");
            intent.putExtras(bundle);
            startActivity(intent);

        }
        else  if (id == R.id.logout) {
            Intent intent = new Intent(SellerDashboard.this,LoginActivity.class);;
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
