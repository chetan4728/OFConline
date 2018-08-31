package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleosystechnologies.ofconline.Activity.AboutUs;
import com.nucleosystechnologies.ofconline.Activity.AdvertiseListing;
import com.nucleosystechnologies.ofconline.Activity.ContactUs;
import com.nucleosystechnologies.ofconline.Activity.Dashboard;
import com.nucleosystechnologies.ofconline.Activity.PackageActivity;
import com.nucleosystechnologies.ofconline.Activity.SellerDashboard;
import com.nucleosystechnologies.ofconline.Activity.Services;
import com.nucleosystechnologies.ofconline.Activity.SubCategory;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class menu_content_adapter extends BaseAdapter{
    Context context;
    ArrayList<String> MenuName;
    ArrayList<Integer> ImageList;
    AppSharedPreferences app;

    public menu_content_adapter(Context context, ArrayList<String> menuName, ArrayList<Integer> imageList) {
        this.context = context;
        this.MenuName = menuName;
        this.ImageList = imageList;
        app =  new AppSharedPreferences(context);
    }

    @Override
    public int getCount() {
        return MenuName.size();
    }

    @Override
    public Object getItem(int i) {
        return MenuName.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if(view==null)
        {
            LayoutInflater inflater =  (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.navigation_item_list,null);
        }
        final String catname = MenuName.get(i);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(catname.trim().equals("About Us"))
                {
                    Intent i = new Intent(context, AboutUs.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                else if(catname.trim().equals("Home"))
                {
                    Intent i = new Intent(context, Dashboard.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }
                else if(catname.trim().equals("Services"))
                {
                    Intent i = new Intent(context, Services.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }

                else if(catname.trim().equals("Seller Section"))
                {
                    Intent i = new Intent(context, SellerDashboard.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }
                else if(catname.trim().equals("Contact Us"))
                {
                    Intent i = new Intent(context, ContactUs.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                else if(catname.trim().equals("Advertisement"))
                {
                    String status = app.pref.getString(app.PAYMENT_STATUS,"");
                    if(status=="null")
                    {
                        Toast.makeText(context, "Please Purchase Aid Package", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent i = new Intent(context, AdvertiseListing.class);
                        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
                else if(catname.trim().equals("Package"))
                {
                    Intent i = new Intent(context, PackageActivity.class);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }




            }
        });

        ImageView imgIcon = (ImageView) view.findViewById(R.id.img);

        imgIcon.setImageResource(ImageList.get(i));

        TextView txtTitle = (TextView) view.findViewById(R.id.textme);
        txtTitle.setText(MenuName.get(i));

        return view;
    }
}
