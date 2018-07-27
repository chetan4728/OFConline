package com.nucleosystechnologies.ofconline.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleosystechnologies.ofconline.Adapter.SectionPagerAdapter;
import com.nucleosystechnologies.ofconline.Adapter.menu_content_adapter;
import com.nucleosystechnologies.ofconline.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<String> MenuName;
    ArrayList<Integer> ImageList;
    private ViewPager viewPager;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView MenuIcon = (ListView)findViewById(R.id.MenuIcon);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));



        TextView nameTitle = (TextView)findViewById(R.id.nameTitle);

        nameTitle.setText("Hello User");


        MenuName = new ArrayList<>();
        ImageList = new ArrayList<>();

        MenuName.add("Home");
        MenuName.add("About Us");
        MenuName.add("Services");
        MenuName.add("Contact Us");
        ImageList.add(R.drawable.home);
        ImageList.add(R.drawable.about);
        ImageList.add(R.drawable.service);
        ImageList.add(R.drawable.contact);

        menu_content_adapter  menu_content_adapter =  new menu_content_adapter(getApplicationContext(),MenuName, ImageList);
        MenuIcon.setAdapter(menu_content_adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
