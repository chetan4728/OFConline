package com.nucleosystechnologies.ofconline.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.nucleosystechnologies.ofconline.Adapter.CategoryAdapter;
import com.nucleosystechnologies.ofconline.Adapter.HomePagerSlider;
import com.nucleosystechnologies.ofconline.Adapter.menu_content_adapter;
import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
import com.nucleosystechnologies.ofconline.Utility.MyGridView;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<String> MenuName;
    ArrayList<CategoryModel> Datalist;
    ArrayList<Integer> ImageList;
    ArrayList<Addvertise_model> AddData;
    private ViewPager Viewpager;

    AppSharedPreferences sharedPreferences;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    String Full_name;
    private android.app.ActionBar actionBar;
    private String[] tabs = { "Top Rated", "Games", "Movies" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        sharedPreferences =  new AppSharedPreferences(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView MenuIcon = (ListView)findViewById(R.id.MenuIcon);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        View header=navigationView.getHeaderView(0);
        TextView headrmobile = (TextView)header.findViewById(R.id.headrmobile);
        headrmobile.setText("+91 "+sharedPreferences.pref.getString(sharedPreferences.Mobile,""));

        EditText serach =  (EditText)findViewById(R.id.serach);
        serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this,SerachCategory.class);
                startActivity(i);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        TextView nameTitle = (TextView)findViewById(R.id.nameTitle);

        nameTitle.setText("Hello User");
        Viewpager = (ViewPager)findViewById(R.id.vp_slider);

        MenuName = new ArrayList<>();
        ImageList = new ArrayList<>();

        MenuName.add("Home");
        MenuName.add("Services");
        MenuName.add("Contact Us");
        MenuName.add("Seller Section");
        ImageList.add(R.drawable.home);
        ImageList.add(R.drawable.about);
        ImageList.add(R.drawable.contact);
        ImageList.add(R.drawable.seller);


        menu_content_adapter  menu_content_adapter =  new menu_content_adapter(getApplicationContext(),MenuName, ImageList);
        MenuIcon.setAdapter(menu_content_adapter);



        final MyGridView categoryList = (MyGridView)findViewById(R.id.categoryList);

        Datalist = new ArrayList<>();
        AddData =  new ArrayList<>();
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


                                CategoryAdapter categoryAdapter =  new CategoryAdapter(getApplicationContext(),Datalist);
                                categoryList.setAdapter(categoryAdapter);


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
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        loadSlider();
        TextView service = (TextView)findViewById(R.id.service);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(Dashboard.this, ""+sharedPreferences.pref.getString(sharedPreferences.FirstName,""), Toast.LENGTH_SHORT).show();
                if(sharedPreferences.pref.getString(sharedPreferences.mast_id,"").isEmpty()) {
                    Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else
                {
                    if(sharedPreferences.pref.getString(sharedPreferences.isSignup,"").isEmpty()) {
                        Intent intent = new Intent(Dashboard.this, UserProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                    else
                    {
                        Intent intent = new Intent(Dashboard.this, SellerDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
            }
        });
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
            Intent intent = new Intent(Dashboard.this,UserProfile.class);
            Bundle bundle = new Bundle();
            bundle.putString("full_name","Hello User");
            intent.putExtras(bundle);
            startActivity(intent);

        }
        else  if (id == R.id.logout) {
            Intent intent = new Intent(Dashboard.this,LoginActivity.class);;
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void loadSlider()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.ADDVERTISE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {

                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    Addvertise_model model =  new Addvertise_model();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setAdd_name(jsonArray.getJSONObject(i).getString("name"));
                                    model.setImg(jsonArray.getJSONObject(i).getString("image"));

                                    AddData.add(model);
                                }

                                HomePagerSlider advertiseAdapter = new HomePagerSlider(getApplicationContext(),AddData);

                                Viewpager.setAdapter(advertiseAdapter);
                                int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                                int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                                int mar = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
                                Viewpager.setPadding(left, 0, right, 0);
                                Viewpager.setClipToPadding(false);
                                Viewpager.setPageMargin(mar);
                                Viewpager.setCurrentItem(AddData.size());

                                final Handler handler = new Handler();
                                final Runnable Update = new Runnable() {
                                    public void run() {
                                        if (currentPage == AddData.size()) {
                                            currentPage = 0;
                                        }
                                        Viewpager.setCurrentItem(currentPage++, true);
                                    }
                                };

                                timer = new Timer(); // This will create a new Thread
                                timer .schedule(new TimerTask() { // task to be scheduled

                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, DELAY_MS, PERIOD_MS);

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
